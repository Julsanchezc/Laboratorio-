# Laboratorio — Estructuras de Datos 2026-I (UNAL)

Taller de estructuras de datos lineales: implementación, benchmark de rendimiento y análisis gráfico comparativo.

---

## Estructuras implementadas

### Listas enlazadas — `IList`

| Clase | `pushFront` | `pushBack` | `popFront` | `popBack` | `find` / `erase` |
|---|:---:|:---:|:---:|:---:|:---:|
| `SinglyLinkedList` | O(1) | O(n) | O(1) | O(n) | O(n) |
| `SinglyLinkedListWithTail` | O(1) | O(1) | O(1) | O(n) | O(n) |
| `DoublyLinkedList` | O(1) | O(n) | O(1) | O(1) | O(n) |
| `DoublyLinkedListWithTail` | O(1) | O(1) | O(1) | O(1) | O(n) |

### Pila — `IStack<T>`

| Clase | `push` | `pop` | `peek` | `delete` |
|---|:---:|:---:|:---:|:---:|
| `ArrayStack<T>` | O(1)* | O(1) | O(1) | O(n) |

### Cola — `IQueue<T>`

| Clase | `enqueue` | `dequeue` | `front` | `delete` |
|---|:---:|:---:|:---:|:---:|
| `ArrayQueue<T>` | O(1)* | O(1) | O(1) | O(n) |

\* amortizado — el arreglo interno se duplica cuando se llena.

---

## Requisitos

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 |
| Python | 3.9 |
| Git Bash (Windows) | cualquiera |

> En Windows se requiere Git Bash para ejecutar `start.sh`.  
> En Linux/Mac, si la compilación falla, reemplaza el bloque de compilación en `start.sh` por:
> ```bash
> echo "Compilando Java..."
> OUT="$ROOT/out/classes"
> mkdir -p "$OUT"
> find "$ROOT/app" -name "*.java" > "$ROOT/out/sources.txt"
> javac -encoding UTF-8 -d "$OUT" @"$ROOT/out/sources.txt"
> echo "Compilación exitosa"
> ```

---

## Inicio rápido

```bash
# 1. Clonar el repositorio
git clone <url-del-repo>
cd Laboratorio-

# 2. Copiar la configuración
cp .env.example .env

# 3. Ejecutar todo (benchmark Java + gráficos Python)
bash start.sh
```

`start.sh` hace automáticamente:
1. Detecta Python del sistema y crea el entorno virtual `venv/`
2. Instala `matplotlib` y `pandas` en el venv
3. Compila todos los `.java` de `app/`
4. Ejecuta el benchmark → genera `results/benchmark_results.csv`
5. Genera 15 gráficos PNG → `results/graphs/`

---

## Configuración

Edita `.env` (nunca se sube al repositorio):

```env
# Tamaños de entrada para el benchmark (separados por coma, sin espacios)
BENCHMARK_SIZES=10,100,10000,1000000

# Repeticiones para calcular tiempos promedio
BENCHMARK_REPETITIONS=100

# Nivel de log: INFO | WARNING | FINE | SEVERE
LOG_LEVEL=INFO
```

> Para tamaños de 100,000,000 el benchmark puede tardar horas con operaciones O(n).
> Se recomienda `BENCHMARK_SIZES=10,100,10000,1000000` para un tiempo razonable (~2 min).

---

## Ejecución manual

**Solo Java (sin gráficos):**
```bash
bash run.sh
```

**Solo gráficos (requiere que ya exista el CSV):**
```bash
venv/Scripts/python analysis/plot_lists.py
venv/Scripts/python analysis/plot_stack_queue.py
venv/Scripts/python analysis/plot_comparison.py
```

---

## Estructura del proyecto

```
Laboratorio-/
├── .env.example                   ← plantilla de configuración
├── .gitignore
├── requirements.txt               ← dependencias Python
├── start.sh                       ← orquestador principal (Java + Python)
├── run.sh                         ← solo compilar y ejecutar Java
├── pom.xml                        ← referencia Maven (no requerido para compilar)
│
├── app/                           ← código fuente Java
│   ├── core/
│   │   ├── AppConfig.java         ← singleton: lee variables desde .env
│   │   └── AppLogger.java         ← singleton: logger global → logs/logs.log
│   ├── datastructures/
│   │   ├── nodes/
│   │   │   └── Node.java          ← nodo compartido (value, next, prev)
│   │   ├── lists/
│   │   │   ├── IList.java
│   │   │   ├── SinglyLinkedList.java
│   │   │   ├── SinglyLinkedListWithTail.java
│   │   │   ├── DoublyLinkedList.java
│   │   │   └── DoublyLinkedListWithTail.java
│   │   ├── stack/
│   │   │   ├── IStack.java
│   │   │   └── ArrayStack.java
│   │   └── queue/
│   │       ├── IQueue.java
│   │       └── ArrayQueue.java    ← arreglo circular dinámico
│   └── benchmark/
│       ├── Benchmark.java         ← utilidades de medición con System.nanoTime()
│       ├── ListBenchmark.java
│       ├── StackBenchmark.java
│       ├── QueueBenchmark.java
│       ├── ResultCollector.java   ← acumula resultados en memoria → exporta CSV
│       └── Main.java              ← punto de entrada
│
├── analysis/                      ← scripts Python de visualización
│   ├── plot_lists.py              ← Parte 1: gráficos por implementación de lista
│   ├── plot_stack_queue.py        ← Parte 2: Stack vs Queue
│   └── plot_comparison.py         ← Parte 3: comparativa List vs Stack vs Queue
│
├── logs/                          ← generado en runtime (gitignored)
│   └── logs.log                   ← se sobreescribe en cada ejecución
├── results/                       ← generado en runtime (gitignored)
│   ├── benchmark_results.csv      ← se sobreescribe en cada ejecución
│   └── graphs/                    ← 15 PNG generados por los scripts Python
└── out/                           ← clases Java compiladas (gitignored)
```

---

## Salidas generadas

### CSV — `results/benchmark_results.csv`

| Columna | Descripción |
|---|---|
| `structure` | Nombre de la estructura (`SinglyLinkedList`, `ArrayStack`, etc.) |
| `operation` | Operación medida (`pushFront`, `enqueue`, `find`, etc.) |
| `size` | Tamaño de entrada `n` |
| `time_ns` | Tiempo promedio en nanosegundos |
| `time_us` | Tiempo promedio en microsegundos |

### Gráficos — `results/graphs/`

| Archivo | Contenido |
|---|---|
| `SinglyLinkedList.png` | Todas las operaciones vs `n` (escala log-log) |
| `SinglyLinkedListWithTail.png` | ídem |
| `DoublyLinkedList.png` | ídem |
| `DoublyLinkedListWithTail.png` | ídem |
| `compare_pushFront.png` | Las 4 listas comparadas para `pushFront` |
| `compare_pushBack.png` | ídem para `pushBack` |
| `compare_popFront.png` | ídem para `popFront` |
| `compare_popBack.png` | ídem para `popBack` |
| `compare_find.png` | ídem para `find` |
| `compare_erase.png` | ídem para `erase` |
| `compare_addBefore.png` | ídem para `addBefore` |
| `compare_addAfter.png` | ídem para `addAfter` |
| `ArrayStack.png` | Todas las operaciones de Stack vs `n` |
| `ArrayQueue.png` | Todas las operaciones de Queue vs `n` |
| `stack_vs_queue.png` | Stack vs Queue en operaciones equivalentes |
| `comparison_01.png` | Inserción al frente: `pushFront` vs `push` |
| `comparison_02.png` | Inserción al final: `pushBack` vs `enqueue` |
| `comparison_03.png` | Eliminación al frente: `popFront` vs `pop`/`dequeue` |
| `comparison_04.png` | Búsqueda: `find` vs `peek`/`front` |
| `comparison_05.png` | Eliminación por valor: `erase` vs `delete` |

---

## Decisiones de diseño

- **Medición separada de I/O**: `ResultCollector` acumula todos los resultados en memoria y exporta el CSV *después* de que termina todo el timing, para no contaminar las mediciones con operaciones de disco.
- **Semilla fija** (`Random(42)`): todos los benchmarks usan la misma semilla aleatoria para garantizar reproducibilidad entre ejecuciones.
- **Nodo compartido**: `Node.java` unifica `next` y `prev` en un solo nodo, eliminando duplicación entre listas simples y dobles. Las listas simples simplemente no usan `prev`.
- **Arreglo circular** (`ArrayQueue`): mantiene punteros `head` y `tail` con aritmética modular para lograr `enqueue`/`dequeue` en O(1) sin desplazar elementos.
