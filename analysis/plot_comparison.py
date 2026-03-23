"""
Parte 3 del análisis — Comparativa List vs Stack / Queue.

Para cada operación equivalente se elige la implementación de List
más óptima y se compara con ArrayStack o ArrayQueue.

Produce dos versiones:
  results/graphs/linear/ — eje Y lineal (escala real)
  results/graphs/log/    — eje Y logarítmico (log-log)

Tabla de equivalencias:
  pushFront  ↔  Stack.push       (mejor List: SinglyLinkedListWithTail  — O(1))
  pushBack   ↔  Queue.enqueue    (mejor List: DoublyLinkedListWithTail  — O(1))
  popFront   ↔  Stack.pop        (mejor List: SinglyLinkedListWithTail  — O(1))
  popFront   ↔  Queue.dequeue    (mejor List: SinglyLinkedListWithTail  — O(1))
  find       ↔  Stack.peek       (conceptualmente: acceso a un elemento)
  erase      ↔  Stack.delete     (mejor List: DoublyLinkedListWithTail  — O(n))
  erase      ↔  Queue.delete     (mejor List: DoublyLinkedListWithTail  — O(n))

Uso:
    python analysis/plot_comparison.py
"""

import pandas as pd
import matplotlib.pyplot as plt
import os

CSV_PATH = "results/benchmark_results.csv"
BASE_DIR = "results/graphs"

SCALES = [
    {"name": "linear", "yscale": "linear", "ylabel_suffix": ""},
    {"name": "log",    "yscale": "log",    "ylabel_suffix": " — Escala Logarítmica"},
]

COMPARISONS = [
    (
        "Inserción al frente: pushFront (List) vs push (Stack)",
        [
            ("SinglyLinkedListWithTail", "pushFront", "List.pushFront\n(SinglyWithTail)"),
            ("DoublyLinkedListWithTail", "pushFront", "List.pushFront\n(DoublyWithTail)"),
            ("ArrayStack",              "push",       "Stack.push"),
        ],
    ),
    (
        "Inserción al final: pushBack (List) vs enqueue (Queue)",
        [
            ("DoublyLinkedListWithTail", "pushBack", "List.pushBack\n(DoublyWithTail)"),
            ("SinglyLinkedListWithTail", "pushBack", "List.pushBack\n(SinglyWithTail)"),
            ("ArrayQueue",              "enqueue",   "Queue.enqueue"),
        ],
    ),
    (
        "Eliminación al frente: popFront (List) vs pop (Stack) / dequeue (Queue)",
        [
            ("DoublyLinkedListWithTail", "popFront", "List.popFront\n(DoublyWithTail)"),
            ("ArrayStack",              "pop",       "Stack.pop"),
            ("ArrayQueue",             "dequeue",    "Queue.dequeue"),
        ],
    ),
    (
        "Búsqueda/acceso: find (List) vs peek (Stack) vs front (Queue)",
        [
            ("DoublyLinkedListWithTail", "find",  "List.find\n(DoublyWithTail)"),
            ("ArrayStack",              "peek",   "Stack.peek"),
            ("ArrayQueue",             "front",   "Queue.front"),
        ],
    ),
    (
        "Eliminación por valor: erase (List) vs delete (Stack) vs delete (Queue)",
        [
            ("DoublyLinkedListWithTail", "erase",  "List.erase\n(DoublyWithTail)"),
            ("ArrayStack",              "delete",  "Stack.delete"),
            ("ArrayQueue",             "delete",   "Queue.delete"),
        ],
    ),
]

MARKERS    = ["o", "s", "^", "D", "v"]
LINESTYLES = ["-", "--", "-."]
COLORS     = ["#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd"]


def main():
    if not os.path.exists(CSV_PATH):
        print(f"[ERROR] No se encontró {CSV_PATH}. Ejecutá primero: bash start.sh")
        return

    df = pd.read_csv(CSV_PATH)
    df["time_us"] = df["time_ns"] / 1_000.0

    for scale in SCALES:
        out_dir = os.path.join(BASE_DIR, scale["name"])
        os.makedirs(out_dir, exist_ok=True)
        yscale = scale["yscale"]
        suffix = scale["ylabel_suffix"]

        for i, (title, series) in enumerate(COMPARISONS):
            fig, ax = plt.subplots(figsize=(10, 6))

            for (struct, op, label), marker, ls, color in zip(series, MARKERS, LINESTYLES, COLORS):
                d = df[(df["structure"] == struct) & (df["operation"] == op)].sort_values("size")
                if d.empty:
                    print(f"  [WARN] sin datos para {struct}.{op}")
                    continue
                ax.plot(d["size"], d["time_us"], marker=marker, linestyle=ls,
                        label=label, color=color)

            ax.set_xscale("log")
            ax.set_yscale(yscale)
            if yscale == "linear":
                ax.set_ylim(bottom=0)
            ax.set_xlabel("Tamaño de entrada (n)", fontsize=12)
            ax.set_ylabel(f"Microsegundos [µs]{suffix}", fontsize=11)
            ax.set_title(title, fontsize=12, fontweight="bold")
            ax.legend(fontsize=9)
            ax.grid(True, which="both", linestyle="--", alpha=0.4)

            fpath = os.path.join(out_dir, f"comparison_{i+1:02d}.png")
            fig.tight_layout()
            fig.savefig(fpath, dpi=150)
            plt.close(fig)
            print(f"[OK] {fpath}  —  {title[:60]}")

        print(f"\nGráficos [{scale['name']}] guardados en: {out_dir}/")


if __name__ == "__main__":
    main()
