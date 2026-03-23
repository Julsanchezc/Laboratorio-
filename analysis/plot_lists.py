"""
Parte 1 del análisis — Comparativa de implementaciones de List.

Genera una figura por cada implementación de lista, mostrando todas las
operaciones en función del tamaño de entrada.

Produce dos versiones:
  results/graphs/linear/ — eje Y lineal (escala real)
  results/graphs/log/    — eje Y logarítmico (log-log)

Uso:
    python analysis/plot_lists.py
"""

import math
import os

import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import pandas as pd

CSV_PATH   = "results/benchmark_results.csv"
BASE_DIR   = "results/graphs"

SCALES = [
    {"name": "linear", "yscale": "linear", "ylabel_suffix": ""},
    {"name": "log",    "yscale": "log",    "ylabel_suffix": " — Escala Logarítmica"},
]

LIST_IMPLS = [
    "SinglyLinkedList",
    "SinglyLinkedListWithTail",
    "DoublyLinkedList",
    "DoublyLinkedListWithTail",
]

OPERATIONS = ["pushFront", "pushBack", "popFront", "popBack",
              "find", "erase", "addBefore", "addAfter"]

COLORS = {
    "pushFront": "#1f77b4",
    "pushBack":  "#ff7f0e",
    "popFront":  "#2ca02c",
    "popBack":   "#d62728",
    "find":      "#9467bd",
    "erase":     "#8c564b",
    "addBefore": "#e377c2",
    "addAfter":  "#7f7f7f",
}

MARKERS = {
    "pushFront": "o",
    "pushBack":  "s",
    "popFront":  "^",
    "popBack":   "D",
    "find":      "v",
    "erase":     "P",
    "addBefore": "X",
    "addAfter":  "*",
}

LINESTYLES = {
    "pushFront": "-",
    "pushBack":  "--",
    "popFront":  "-",
    "popBack":   "--",
    "find":      "-.",
    "erase":     ":",
    "addBefore": "-.",
    "addAfter":  ":",
}

NICE_NAMES = {
    "SinglyLinkedList":         "Single Linked List – No Tail",
    "SinglyLinkedListWithTail": "Single Linked List – With Tail",
    "DoublyLinkedList":         "Doubly Linked List – No Tail",
    "DoublyLinkedListWithTail": "Doubly Linked List – With Tail",
}

IMPL_MARKERS    = ["o", "s", "^", "D"]
IMPL_LINESTYLES = ["-", "--", "-.", ":"]


def apply_scale(ax, yscale, ylabel_suffix):
    ax.set_xscale("log")
    ax.set_yscale(yscale)
    if yscale == "linear":
        ax.set_ylim(bottom=0)
    ax.set_xlabel("Tamaño de la lista (n)", fontsize=12)
    ax.set_ylabel(f"Promedio Microsegundos [µs]{ylabel_suffix}", fontsize=11)
    ax.xaxis.set_major_formatter(
        ticker.FuncFormatter(lambda x, _: f"10^{int(round(math.log10(x)))}" if x > 0 else "0"))


def main():
    if not os.path.exists(CSV_PATH):
        print(f"[ERROR] No se encontró {CSV_PATH}.")
        print("        Ejecutá primero: bash start.sh")
        return

    df = pd.read_csv(CSV_PATH)
    df["time_us"] = df["time_ns"] / 1_000.0

    for scale in SCALES:
        out_dir = os.path.join(BASE_DIR, scale["name"])
        os.makedirs(out_dir, exist_ok=True)
        yscale  = scale["yscale"]
        suffix  = scale["ylabel_suffix"]

        # — Una figura por implementación —
        for impl in LIST_IMPLS:
            subset = df[df["structure"] == impl]
            if subset.empty:
                print(f"[WARN] Sin datos para {impl}")
                continue

            fig, ax = plt.subplots(figsize=(10, 6))
            for op in OPERATIONS:
                op_data = subset[subset["operation"] == op].sort_values("size")
                if op_data.empty:
                    continue
                ax.plot(op_data["size"], op_data["time_us"],
                        marker=MARKERS.get(op, "o"),
                        linestyle=LINESTYLES.get(op, "-"),
                        label=op, color=COLORS.get(op))

            apply_scale(ax, yscale, suffix)
            ax.set_title(NICE_NAMES.get(impl, impl), fontsize=14, fontweight="bold")
            ax.legend(loc="upper left", fontsize=9)
            ax.grid(True, which="both", linestyle="--", alpha=0.4)

            fpath = os.path.join(out_dir, f"{impl}.png")
            fig.tight_layout()
            fig.savefig(fpath, dpi=150)
            plt.close(fig)
            print(f"[OK] {fpath}")

        # — Una figura por operación comparando las 4 implementaciones —
        for op in OPERATIONS:
            op_data = df[(df["structure"].isin(LIST_IMPLS)) & (df["operation"] == op)]
            if op_data.empty:
                continue

            fig, ax = plt.subplots(figsize=(10, 5))
            for impl, mk, ls in zip(LIST_IMPLS, IMPL_MARKERS, IMPL_LINESTYLES):
                d = op_data[op_data["structure"] == impl].sort_values("size")
                ax.plot(d["size"], d["time_us"], marker=mk, linestyle=ls,
                        label=NICE_NAMES.get(impl, impl))

            apply_scale(ax, yscale, suffix)
            ax.set_ylabel(f"Microsegundos [µs]{suffix}", fontsize=11)
            ax.set_title(f"Comparativa de implementaciones — {op}", fontsize=13, fontweight="bold")
            ax.legend(fontsize=8)
            ax.grid(True, which="both", linestyle="--", alpha=0.4)

            fpath = os.path.join(out_dir, f"compare_{op}.png")
            fig.tight_layout()
            fig.savefig(fpath, dpi=150)
            plt.close(fig)
            print(f"[OK] {fpath}")

        print(f"\nGráficos [{scale['name']}] guardados en: {out_dir}/")


if __name__ == "__main__":
    main()
