"""
Parte 2 del análisis — MyStack y MyQueue.

Genera una figura para ArrayStack y otra para ArrayQueue, mostrando
todas sus operaciones en función del tamaño de entrada.

Produce dos versiones:
  results/graphs/linear/ — eje Y lineal (escala real)
  results/graphs/log/    — eje Y logarítmico (log-log)

Uso:
    python analysis/plot_stack_queue.py
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

STRUCTURES = {
    "ArrayStack": {
        "title": "ArrayStack (Arreglo Dinámico Circular)",
        "ops":   ["push", "peek", "pop", "delete", "size", "isEmpty"],
    },
    "ArrayQueue": {
        "title": "ArrayQueue (Arreglo Circular Dinámico)",
        "ops":   ["enqueue", "front", "dequeue", "delete", "size", "isEmpty"],
    },
}

COLORS     = ["#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b"]
MARKERS    = ["o", "s", "^", "D", "v", "P"]
LINESTYLES = ["-", "--", "-.", ":", "-", "--"]


def apply_scale(ax, yscale, ylabel_suffix):
    ax.set_xscale("log")
    ax.set_yscale(yscale)
    if yscale == "linear":
        ax.set_ylim(bottom=0)
    ax.set_xlabel("Tamaño de entrada (n)", fontsize=12)
    ax.set_ylabel(f"Promedio Microsegundos [µs]{ylabel_suffix}", fontsize=11)


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

        # — Una figura por estructura —
        for struct, meta in STRUCTURES.items():
            subset = df[df["structure"] == struct]
            if subset.empty:
                print(f"[WARN] Sin datos para {struct}")
                continue

            fig, ax = plt.subplots(figsize=(10, 6))
            for op, color, mk, ls in zip(meta["ops"], COLORS, MARKERS, LINESTYLES):
                op_data = subset[subset["operation"] == op].sort_values("size")
                if op_data.empty:
                    continue
                ax.plot(op_data["size"], op_data["time_us"],
                        marker=mk, linestyle=ls, label=op, color=color)

            apply_scale(ax, yscale, suffix)
            ax.set_title(meta["title"], fontsize=14, fontweight="bold")
            ax.legend(loc="upper left", fontsize=9)
            ax.grid(True, which="both", linestyle="--", alpha=0.4)

            fpath = os.path.join(out_dir, f"{struct}.png")
            fig.tight_layout()
            fig.savefig(fpath, dpi=150)
            plt.close(fig)
            print(f"[OK] {fpath}")

        # — Comparativa Stack vs Queue —
        fig, axes = plt.subplots(1, 2, figsize=(14, 5))
        pairs = [
            ("push", "ArrayStack", "enqueue", "ArrayQueue", axes[0], "Inserción (push / enqueue)"),
            ("pop",  "ArrayStack", "dequeue", "ArrayQueue", axes[1], "Extracción (pop / dequeue)"),
        ]
        for op1, s1, op2, s2, ax, title in pairs:
            d1 = df[(df["structure"] == s1) & (df["operation"] == op1)].sort_values("size")
            d2 = df[(df["structure"] == s2) & (df["operation"] == op2)].sort_values("size")
            if not d1.empty:
                ax.plot(d1["size"], d1["time_us"], marker="o", linestyle="-", label=f"{s1}.{op1}")
            if not d2.empty:
                ax.plot(d2["size"], d2["time_us"], marker="s", linestyle="--", label=f"{s2}.{op2}")
            ax.set_xscale("log")
            ax.set_yscale(yscale)
            if yscale == "linear":
                ax.set_ylim(bottom=0)
            ax.set_xlabel("Tamaño (n)", fontsize=11)
            ax.set_ylabel(f"µs{suffix}", fontsize=11)
            ax.set_title(title, fontsize=12, fontweight="bold")
            ax.legend(fontsize=9)
            ax.grid(True, which="both", linestyle="--", alpha=0.4)

        fpath = os.path.join(out_dir, "stack_vs_queue.png")
        fig.suptitle("Stack vs Queue — Operaciones equivalentes", fontsize=14, fontweight="bold")
        fig.tight_layout()
        fig.savefig(fpath, dpi=150)
        plt.close(fig)
        print(f"[OK] {fpath}")

        print(f"\nGráficos [{scale['name']}] guardados en: {out_dir}/")


if __name__ == "__main__":
    main()
