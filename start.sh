#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# Orquestador principal
# Uso: bash start.sh
#
# Pasos:
#   1. Detecta Python, crea/reutiliza el venv y sincroniza dependencias
#   2. Compila y ejecuta el benchmark Java  → genera results/benchmark_results.csv
#   3. Ejecuta los tres scripts de graficación → genera results/graphs/*.png
# ─────────────────────────────────────────────────────────────────────────────

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
VENV="$ROOT/venv"

# ── Colores ───────────────────────────────────────────────────────────────────
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
RESET='\033[0m'

step() { echo -e "\n${CYAN}▶ $1${RESET}"; }
ok()   { echo -e "${GREEN}  ✔ $1${RESET}"; }
warn() { echo -e "${YELLOW}  ! $1${RESET}"; }
die()  { echo -e "${RED}  ✗ $1${RESET}"; exit 1; }

# ── 1. Detectar Python del sistema ────────────────────────────────────────────
step "Detectando Python del sistema..."

PYTHON_CMD=""
for cmd in python3 python py; do
    if command -v "$cmd" &>/dev/null; then
        # Descarta el alias del Microsoft Store (devuelve error real)
        if "$cmd" -c "import sys; sys.exit(0)" &>/dev/null; then
            PYTHON_CMD="$cmd"
            break
        fi
    fi
done

[ -z "$PYTHON_CMD" ] && die "No se encontró Python. Instalalo desde python.org"
ok "Usando: $PYTHON_CMD ($(${PYTHON_CMD} --version 2>&1))"

# ── 2. Crear o reutilizar el venv ─────────────────────────────────────────────
step "Verificando entorno virtual..."

# El ejecutable dentro del venv varía según SO:
# Windows → venv/Scripts/python   |   Linux/Mac → venv/bin/python
if [ -f "$VENV/Scripts/python" ]; then
    VENV_PYTHON="$VENV/Scripts/python"
elif [ -f "$VENV/bin/python" ]; then
    VENV_PYTHON="$VENV/bin/python"
else
    warn "venv no encontrado — creando en ./venv ..."
    "$PYTHON_CMD" -m venv "$VENV"
    # Determinar el ejecutable tras la creación
    if [ -f "$VENV/Scripts/python" ]; then
        VENV_PYTHON="$VENV/Scripts/python"
    else
        VENV_PYTHON="$VENV/bin/python"
    fi
    ok "venv creado"
fi

ok "venv listo: $VENV_PYTHON"

# ── 3. Sincronizar dependencias ───────────────────────────────────────────────
step "Sincronizando dependencias Python..."
"$VENV_PYTHON" -m pip install --quiet --upgrade pip
"$VENV_PYTHON" -m pip install --quiet -r "$ROOT/requirements.txt"
ok "Dependencias OK"

# ── 4. Java: compilar ─────────────────────────────────────────────────────────
step "Compilando Java..."

ROOT_WIN="$(cd "$ROOT" && pwd -W)"
OUT="$ROOT_WIN/out/classes"

mkdir -p "$ROOT/out/classes"

find "$ROOT/app" -name "*.java" | while IFS= read -r f; do
    cygpath -m "$f"
done | awk '{printf "\"%s\"\n", $0}' > "$ROOT/out/sources.txt"

javac -encoding UTF-8 -d "$OUT" @"$ROOT/out/sources.txt"
ok "Compilación exitosa"

# ── 5. Java: ejecutar benchmark ───────────────────────────────────────────────
step "Ejecutando benchmark Java..."
java -cp "$OUT" app.benchmark.Main
ok "Benchmark completado → results/benchmark_results.csv"

# ── 6. Python: graficación ────────────────────────────────────────────────────
step "Generando gráficos — Parte 1: Listas..."
"$VENV_PYTHON" "$ROOT/analysis/plot_lists.py"

step "Generando gráficos — Parte 2: Stack y Queue..."
"$VENV_PYTHON" "$ROOT/analysis/plot_stack_queue.py"

step "Generando gráficos — Parte 3: Comparativa List vs Stack/Queue..."
"$VENV_PYTHON" "$ROOT/analysis/plot_comparison.py"

# ── Resumen ───────────────────────────────────────────────────────────────────
echo -e "\n${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${RESET}"
echo -e "${GREEN}  Todo listo.${RESET}"
echo -e "  CSV   →  results/benchmark_results.csv"
echo -e "  PNG   →  results/graphs/*.png"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${RESET}\n"
