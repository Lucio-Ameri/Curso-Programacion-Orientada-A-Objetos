import {
  apiGet,
  formatDate,
  formatMoney,
  getSelectedAccount,
  saveSelectedAccount,
  requireUser,
  setFeedback,
  wireLogout
} from "./api.js";

requireUser();
wireLogout();

const params = new URLSearchParams(window.location.search);
const cuentaIdParam = params.get("cuentaId");

const fallbackCuenta = getSelectedAccount();

const selectedAccountInfo = document.getElementById("selectedAccountInfo");
const movesList = document.getElementById("movesList");
const movesEmpty = document.getElementById("movesEmpty");
const refreshBtn = document.getElementById("refreshMovesBtn");

let cuenta = null;

if (!cuentaIdParam && !fallbackCuenta) {
  window.location.href = "/accounts.html";
}

function renderSelectedAccount() {
  if (!cuenta) return;

  selectedAccountInfo.innerHTML = `
    <div class="info-item"><strong>Cuenta ID:</strong> ${cuenta.cuentaId}</div>
    <div class="info-item"><strong>Usuario ID:</strong> ${cuenta.usuarioId}</div>
    <div class="info-item"><strong>Tipo:</strong> ${cuenta.tipoCuenta}</div>
    <div class="info-item"><strong>Saldo:</strong> ${formatMoney(cuenta.saldo, cuenta.moneda)}</div>
    <div class="info-item"><strong>Moneda:</strong> ${cuenta.moneda}</div>
  `;
}

async function loadAccount() {
  try {
    const cuentaId = cuentaIdParam || fallbackCuenta?.cuentaId;

    if (!cuentaId) {
      window.location.href = "/accounts.html";
      return;
    }

    cuenta = await apiGet(`/api/cuentas/${cuentaId}`);
    saveSelectedAccount(cuenta);
    renderSelectedAccount();
  } catch (error) {
    setFeedback("moneyMessage", error.message, true);
  }
}

async function loadMoves() {
  if (!cuenta) return;

  movesList.innerHTML = "";
  setFeedback("moneyMessage", "");

  try {
    const movimientos = await apiGet(`/api/cuentas/${cuenta.cuentaId}/movimientos`);

    if (!movimientos || movimientos.length === 0) {
      movesEmpty.classList.remove("hidden");
      return;
    }

    movesEmpty.classList.add("hidden");

    movimientos.forEach((mov) => {
      const item = document.createElement("article");
      item.className = "list-item";

      item.innerHTML = `
        <h3>Movimiento #${mov.movimientoId} <span class="badge">${mov.tipoMovimiento}</span></h3>
        <div class="info-grid">
          <div class="info-item"><strong>Monto:</strong> ${formatMoney(mov.monto, mov.moneda)}</div>
          <div class="info-item"><strong>Fecha:</strong> ${formatDate(mov.fecha)}</div>
          <div class="info-item"><strong>Cuenta:</strong> ${mov.cuentaId}</div>
        </div>
      `;

      movesList.appendChild(item);
    });
  } catch (error) {
    setFeedback("moneyMessage", error.message, true);
  }
}

refreshBtn.addEventListener("click", async () => {
  await loadAccount();
  await loadMoves();
});

(async function init() {
  await loadAccount();
  await loadMoves();
})();
