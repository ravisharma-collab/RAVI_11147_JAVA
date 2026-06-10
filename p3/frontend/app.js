/**
 * app.js – Smart Warehouse Grid Locator
 * Frontend logic in plain JavaScript.
 * Mirrors the Java backend OOP structure:
 *   GridItem class  →  JS GridItem class
 *   Warehouse class →  JS Warehouse class
 *   Main            →  DOMContentLoaded initialisation
 */

'use strict';

/* ═══════════════════════════════════════════
   CLASS: GridItem   (mirrors Java GridItem)
═══════════════════════════════════════════ */
class GridItem {
  /**
   * @param {string} itemId
   * @param {string} itemName
   * @param {number} quantity
   */
  constructor(itemId, itemName, quantity) {
    this.setItemId(itemId);
    this.setItemName(itemName);
    this.setQuantity(quantity);
    this.createdAt = new Date();   // extra: timestamp
  }

  // Setters with validation (Encapsulation)
  setItemId(id) {
    if (!id || id.trim() === '') throw new Error('Item ID cannot be empty.');
    this.itemId = id.trim().toUpperCase();
  }
  setItemName(name) {
    if (!name || name.trim() === '') throw new Error('Item name cannot be empty.');
    this.itemName = name.trim();
  }
  setQuantity(qty) {
    const n = Number(qty);
    if (isNaN(n) || n < 0) throw new Error('Quantity must be a non-negative number.');
    this.quantity = n;
  }

  toString() {
    return `GridItem { itemId='${this.itemId}', itemName='${this.itemName}', quantity=${this.quantity} }`;
  }
}

/* ═══════════════════════════════════════════
   CLASS: Warehouse  (mirrors Java Warehouse)
═══════════════════════════════════════════ */
class Warehouse {
  /**
   * @param {number} rows
   * @param {number} cols
   */
  constructor(rows, cols) {
    this.rows = rows;
    this.cols = cols;
    // 2D array – null means empty
    this.grid = Array.from({ length: rows }, () => new Array(cols).fill(null));
  }

  // ── addItem ──────────────────────────────
  addItem(itemId, itemName, quantity, row, col) {
    if (!this.isValidPosition(row, col))
      return { ok: false, msg: `Invalid position (${row}, ${col}). Must be within 0–${this.rows - 1} × 0–${this.cols - 1}.` };

    if (this.grid[row][col] !== null)
      return { ok: false, msg: `Slot (${row}, ${col}) is already occupied by "${this.grid[row][col].itemId}".` };

    if (this.searchItemById(itemId))
      return { ok: false, msg: `Item ID "${itemId.toUpperCase()}" already exists in the warehouse.` };

    try {
      const item = new GridItem(itemId, itemName, quantity);
      this.grid[row][col] = item;
      return { ok: true, msg: `Item "${item.itemId}" added at (${row}, ${col}).`, item };
    } catch (e) {
      return { ok: false, msg: e.message };
    }
  }

  // ── searchItemById ───────────────────────
  searchItemById(itemId) {
    if (!itemId) return null;
    const id = itemId.trim().toUpperCase();
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (this.grid[r][c] && this.grid[r][c].itemId === id) {
          return { item: this.grid[r][c], row: r, col: c };
        }
      }
    }
    return null;
  }

  // ── searchItemByName ─────────────────────
  searchItemByName(name) {
    const results = [];
    if (!name) return results;
    const keyword = name.trim().toLowerCase();
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (this.grid[r][c] && this.grid[r][c].itemName.toLowerCase().includes(keyword)) {
          results.push({ item: this.grid[r][c], row: r, col: c });
        }
      }
    }
    return results;
  }

  // ── removeItem ───────────────────────────
  removeItem(itemId) {
    const found = this.searchItemById(itemId);
    if (!found) return { ok: false, msg: `Item "${itemId.toUpperCase()}" not found.` };
    this.grid[found.row][found.col] = null;
    return { ok: true, msg: `Item "${itemId.toUpperCase()}" removed from (${found.row}, ${found.col}).` };
  }

  // ── updateQuantity ───────────────────────
  updateQuantity(itemId, newQty) {
    if (newQty < 0) return { ok: false, msg: 'Quantity cannot be negative.' };
    const found = this.searchItemById(itemId);
    if (!found) return { ok: false, msg: `Item "${itemId.toUpperCase()}" not found.` };
    const old = found.item.quantity;
    found.item.setQuantity(newQty);
    return { ok: true, msg: `"${found.item.itemId}" quantity updated from ${old} → ${newQty}.` };
  }

  // ── getEmptySlots ────────────────────────
  getEmptySlots() {
    const slots = [];
    for (let r = 0; r < this.rows; r++)
      for (let c = 0; c < this.cols; c++)
        if (!this.grid[r][c]) slots.push({ row: r, col: c });
    return slots;
  }

  // ── getAllItems ──────────────────────────
  getAllItems() {
    const items = [];
    for (let r = 0; r < this.rows; r++)
      for (let c = 0; c < this.cols; c++)
        if (this.grid[r][c]) items.push({ item: this.grid[r][c], row: r, col: c });
    return items;
  }

  // ── Statistics helpers ───────────────────
  getOccupiedCount()       { return this.getAllItems().length; }
  getOccupancyPct()        { return (this.getOccupiedCount() / (this.rows * this.cols)) * 100; }
  getTotalQuantity()       { return this.getAllItems().reduce((s, e) => s + e.item.quantity, 0); }
  isValidPosition(r, c)    { return r >= 0 && r < this.rows && c >= 0 && c < this.cols; }
}

/* ═══════════════════════════════════════════
   APPLICATION STATE
═══════════════════════════════════════════ */
const warehouse    = new Warehouse(5, 5);
const recentItems  = [];      // last 10 added
const searchHistory = [];     // last 20 searches
let   currentModal = null;    // { row, col } of cell being edited
let   chartInstance = null;

/* ═══════════════════════════════════════════
   INITIALISATION  (mirrors Java Main.java)
═══════════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', () => {
  // Seed sample data
  const samples = [
    ['I101', 'Laptop',      50,  0, 0],
    ['I102', 'Phone',       25,  1, 2],
    ['I103', 'Tablet',      30,  2, 1],
    ['I104', 'Monitor',     15,  0, 4],
    ['I105', 'Keyboard',   100,  3, 3],
    ['I106', 'Mouse',       80,  4, 1],
    ['I107', 'USB Hub',     60,  2, 4],
    ['I108', 'Webcam',      40,  1, 0],
    ['I109', 'Headphones',  20,  3, 2],
    ['I110', 'SSD Drive',   35,  4, 4],
  ];
  samples.forEach(([id, name, qty, r, c]) => {
    const res = warehouse.addItem(id, name, qty, r, c);
    if (res.ok) pushRecent(res.item);
  });

  // Wire up the Add Item form
  document.getElementById('addItemForm').addEventListener('submit', handleAddItem);

  // Dark mode toggle
  document.getElementById('darkModeToggle').addEventListener('click', toggleDarkMode);

  // Initial render
  renderGrid();
  refreshStats();
  renderChart();
  renderAllItems();
  renderRecent();
});

/* ═══════════════════════════════════════════
   ADD ITEM
═══════════════════════════════════════════ */
function handleAddItem(e) {
  e.preventDefault();

  const itemId   = document.getElementById('itemId').value;
  const itemName = document.getElementById('itemName').value;
  const quantity = parseInt(document.getElementById('quantity').value, 10);
  const row      = parseInt(document.getElementById('row').value, 10);
  const col      = parseInt(document.getElementById('col').value, 10);

  // Client-side validation
  if (!itemId || !itemName) { showToast('Item ID and Name are required.', 'error'); return; }
  if (isNaN(quantity) || quantity < 0) { showToast('Quantity must be ≥ 0.', 'error'); return; }
  if (isNaN(row) || isNaN(col))        { showToast('Row and Column are required.', 'error'); return; }

  const result = warehouse.addItem(itemId, itemName, quantity, row, col);

  if (result.ok) {
    showToast(`✅ ${result.msg}`);
    pushRecent(result.item);
    document.getElementById('addItemForm').reset();
    renderGrid();
    refreshStats();
    renderChart();
    renderAllItems();
    renderRecent();
  } else {
    showToast(`❌ ${result.msg}`, 'error');
  }
}

/* ═══════════════════════════════════════════
   SEARCH BY ID
═══════════════════════════════════════════ */
function searchById() {
  const id = document.getElementById('searchId').value.trim();
  if (!id) { showToast('Enter an Item ID to search.', 'warning'); return; }

  clearSearch();
  const result = warehouse.searchItemById(id);
  const box    = document.getElementById('searchResult');
  box.classList.remove('hidden', 'error');

  if (result) {
    box.innerHTML =
      `🔍 <strong>Found!</strong><br>
       ID: <strong>${result.item.itemId}</strong><br>
       Name: ${result.item.itemName}<br>
       Quantity: ${result.item.quantity}<br>
       Location: Row <strong>${result.row}</strong>, Col <strong>${result.col}</strong>`;
    highlightCell(result.row, result.col);
    addHistory(`Searched ID: ${id} → Found at (${result.row},${result.col})`);
  } else {
    box.innerHTML = `❌ Item ID "<strong>${id.toUpperCase()}</strong>" not found.`;
    box.classList.add('error');
    addHistory(`Searched ID: ${id} → Not Found`);
  }
}

/* ═══════════════════════════════════════════
   SEARCH BY NAME
═══════════════════════════════════════════ */
function searchByName() {
  const name = document.getElementById('searchName').value.trim();
  if (!name) { showToast('Enter an item name to search.', 'warning'); return; }

  clearSearch();
  const results = warehouse.searchItemByName(name);
  const box     = document.getElementById('searchResult');
  box.classList.remove('hidden', 'error');

  if (results.length > 0) {
    const rows = results.map(r =>
      `▸ <strong>${r.item.itemId}</strong> – ${r.item.itemName} | Qty: ${r.item.quantity} | (${r.row},${r.col})`
    ).join('<br>');
    box.innerHTML = `🔍 <strong>${results.length} match(es) for "${name}":</strong><br>${rows}`;
    results.forEach(r => highlightCell(r.row, r.col));
    addHistory(`Searched name: "${name}" → ${results.length} result(s)`);
  } else {
    box.innerHTML = `❌ No item with name containing "<strong>${name}</strong>" found.`;
    box.classList.add('error');
    addHistory(`Searched name: "${name}" → Not Found`);
  }
}

/* ═══════════════════════════════════════════
   RENDER WAREHOUSE GRID
═══════════════════════════════════════════ */
function renderGrid() {
  const container = document.getElementById('warehouseGrid');
  container.innerHTML = '';

  for (let r = 0; r < warehouse.rows; r++) {
    for (let c = 0; c < warehouse.cols; c++) {
      const cell = document.createElement('div');
      const item = warehouse.grid[r][c];

      cell.className = `grid-cell ${item ? 'occupied' : 'empty'}`;
      cell.dataset.row = r;
      cell.dataset.col = c;
      // Staggered animation delay
      cell.style.animationDelay = `${(r * warehouse.cols + c) * 30}ms`;

      if (item) {
        cell.innerHTML = `
          <span class="row-label">R${r}C${c}</span>
          <span class="cell-id">${item.itemId}</span>
          <span class="cell-name">${item.itemName}</span>
          <span class="cell-qty">×${item.quantity}</span>`;
        cell.title = `${item.itemId} | ${item.itemName} | Qty: ${item.quantity}\nClick to edit`;
        cell.addEventListener('click', () => openModal(r, c));
      } else {
        cell.innerHTML = `
          <span class="row-label">R${r}C${c}</span>
          <span class="cell-empty-label">Empty</span>`;
        cell.title = `Empty slot at (${r}, ${c})`;
      }

      container.appendChild(cell);
    }
  }
}

/* ═══════════════════════════════════════════
   HIGHLIGHT A SPECIFIC CELL
═══════════════════════════════════════════ */
function highlightCell(row, col) {
  const cell = document.querySelector(`[data-row="${row}"][data-col="${col}"]`);
  if (cell) cell.classList.add('highlighted');
}

function clearSearch() {
  document.querySelectorAll('.grid-cell.highlighted')
          .forEach(c => c.classList.remove('highlighted'));
}

/* ═══════════════════════════════════════════
   MODAL – OPEN / CLOSE / CONFIRM
═══════════════════════════════════════════ */
function openModal(row, col) {
  const item = warehouse.grid[row][col];
  if (!item) return;

  currentModal = { row, col };
  document.getElementById('modalItemInfo').innerHTML =
    `<strong>ID:</strong> ${item.itemId}<br>
     <strong>Name:</strong> ${item.itemName}<br>
     <strong>Current Quantity:</strong> ${item.quantity}<br>
     <strong>Location:</strong> Row ${row}, Column ${col}`;
  document.getElementById('newQuantity').value = item.quantity;
  document.getElementById('modal').classList.remove('hidden');
  document.getElementById('newQuantity').focus();
}

function closeModal() {
  document.getElementById('modal').classList.add('hidden');
  currentModal = null;
}

function confirmUpdate() {
  if (!currentModal) return;
  const newQty = parseInt(document.getElementById('newQuantity').value, 10);
  if (isNaN(newQty) || newQty < 0) { showToast('Quantity must be ≥ 0.', 'error'); return; }

  const item   = warehouse.grid[currentModal.row][currentModal.col];
  const result = warehouse.updateQuantity(item.itemId, newQty);

  if (result.ok) {
    showToast(`✅ ${result.msg}`);
    closeModal();
    renderGrid();
    refreshStats();
    renderChart();
    renderAllItems();
  } else {
    showToast(`❌ ${result.msg}`, 'error');
  }
}

function confirmRemove() {
  if (!currentModal) return;
  const item   = warehouse.grid[currentModal.row][currentModal.col];
  const result = warehouse.removeItem(item.itemId);

  if (result.ok) {
    showToast(`🗑 ${result.msg}`);
    closeModal();
    renderGrid();
    refreshStats();
    renderChart();
    renderAllItems();
  } else {
    showToast(`❌ ${result.msg}`, 'error');
  }
}

// Close modal on overlay click
document.addEventListener('click', (e) => {
  if (e.target.id === 'modal') closeModal();
});

/* ═══════════════════════════════════════════
   REFRESH STATS
═══════════════════════════════════════════ */
function refreshStats() {
  document.getElementById('statTotalItems').textContent = warehouse.getOccupiedCount();
  document.getElementById('statOccupied').textContent   = warehouse.getOccupiedCount();
  document.getElementById('statEmpty').textContent      = warehouse.getEmptySlots().length;
  document.getElementById('statQuantity').textContent   = warehouse.getTotalQuantity();
  document.getElementById('statOccupancy').textContent  = warehouse.getOccupancyPct().toFixed(1) + '%';
}

/* ═══════════════════════════════════════════
   UTILISATION CHART (Chart.js doughnut)
═══════════════════════════════════════════ */
function renderChart() {
  const occupied = warehouse.getOccupiedCount();
  const empty    = warehouse.getEmptySlots().length;
  const ctx      = document.getElementById('utilizationChart').getContext('2d');

  if (chartInstance) chartInstance.destroy();

  chartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Occupied', 'Empty'],
      datasets: [{
        data: [occupied, empty],
        backgroundColor: ['rgba(92,158,255,0.75)', 'rgba(255,255,255,0.08)'],
        borderColor:     ['rgba(92,158,255,1)',     'rgba(255,255,255,0.15)'],
        borderWidth: 2,
        hoverOffset: 6,
      }]
    },
    options: {
      cutout: '70%',
      plugins: {
        legend: {
          labels: { color: getComputedStyle(document.body).getPropertyValue('--text-secondary').trim() || '#9fa8da', boxWidth: 12 }
        },
        tooltip: {
          callbacks: {
            label: (ctx) => ` ${ctx.label}: ${ctx.raw} slots (${((ctx.raw / 25) * 100).toFixed(0)}%)`
          }
        }
      },
      animation: { animateRotate: true, duration: 600 }
    }
  });
}

/* ═══════════════════════════════════════════
   TABS
═══════════════════════════════════════════ */
function switchTab(name, btn) {
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
  btn.classList.add('active');
  document.getElementById('tab-' + name).classList.add('active');

  if (name === 'allItems') renderAllItems();
  if (name === 'recent')   renderRecent();
  if (name === 'history')  renderHistory();
}

/* ═══════════════════════════════════════════
   RECENTLY ADDED
═══════════════════════════════════════════ */
function pushRecent(item) {
  recentItems.unshift(item);
  if (recentItems.length > 10) recentItems.pop();
}

function renderRecent() {
  const ul = document.getElementById('recentList');
  ul.innerHTML = recentItems.length
    ? recentItems.map(item =>
        `<li><strong>${item.itemId}</strong> – ${item.itemName} &nbsp;|&nbsp; Qty: ${item.quantity}</li>`
      ).join('')
    : '<li style="opacity:0.5">No items added yet.</li>';
}

/* ═══════════════════════════════════════════
   SEARCH HISTORY
═══════════════════════════════════════════ */
function addHistory(entry) {
  searchHistory.unshift(`[${new Date().toLocaleTimeString()}] ${entry}`);
  if (searchHistory.length > 20) searchHistory.pop();
}

function renderHistory() {
  const ul = document.getElementById('historyList');
  ul.innerHTML = searchHistory.length
    ? searchHistory.map(h => `<li>${h}</li>`).join('')
    : '<li style="opacity:0.5">No searches yet.</li>';
}

/* ═══════════════════════════════════════════
   ALL ITEMS TABLE
═══════════════════════════════════════════ */
function renderAllItems() {
  const tbody = document.getElementById('allItemsBody');
  const all   = warehouse.getAllItems();

  tbody.innerHTML = all.length
    ? all.map(({ item, row, col }) => `
        <tr>
          <td><strong>${item.itemId}</strong></td>
          <td>${item.itemName}</td>
          <td>${item.quantity}</td>
          <td>${row}</td>
          <td>${col}</td>
          <td>
            <button class="btn btn-secondary" onclick="openModal(${row},${col})">Edit</button>
            <button class="btn btn-danger"    onclick="quickRemove('${item.itemId}')">🗑</button>
          </td>
        </tr>`).join('')
    : '<tr><td colspan="6" style="opacity:0.5;text-align:center;padding:1rem;">No items in warehouse.</td></tr>';
}

function quickRemove(itemId) {
  const result = warehouse.removeItem(itemId);
  if (result.ok) {
    showToast(`🗑 ${result.msg}`);
    renderGrid(); refreshStats(); renderChart(); renderAllItems();
  } else {
    showToast(`❌ ${result.msg}`, 'error');
  }
}

/* ═══════════════════════════════════════════
   EXPORT REPORT
═══════════════════════════════════════════ */
function exportReport() {
  const all  = warehouse.getAllItems();
  const date = new Date().toLocaleString();

  let report = `SMART WAREHOUSE GRID LOCATOR – REPORT\n`;
  report    += `Generated: ${date}\n`;
  report    += `Grid Size: ${warehouse.rows}×${warehouse.cols}\n`;
  report    += `Occupancy: ${warehouse.getOccupancyPct().toFixed(1)}%\n`;
  report    += `Occupied: ${warehouse.getOccupiedCount()} | Empty: ${warehouse.getEmptySlots().length}\n`;
  report    += `Total Quantity: ${warehouse.getTotalQuantity()}\n`;
  report    += '\n' + '─'.repeat(52) + '\n';
  report    += `${'Item ID'.padEnd(10)} ${'Name'.padEnd(18)} ${'Qty'.padStart(6)} ${'Row'.padStart(5)} ${'Col'.padStart(5)}\n`;
  report    += '─'.repeat(52) + '\n';

  all.forEach(({ item, row, col }) => {
    report += `${item.itemId.padEnd(10)} ${item.itemName.padEnd(18)} ${String(item.quantity).padStart(6)} ${String(row).padStart(5)} ${String(col).padStart(5)}\n`;
  });

  report += '─'.repeat(52) + '\n';
  report += `${'TOTAL'.padEnd(28)} ${String(warehouse.getTotalQuantity()).padStart(6)}\n`;

  // Download as .txt
  const blob = new Blob([report], { type: 'text/plain' });
  const url  = URL.createObjectURL(blob);
  const a    = document.createElement('a');
  a.href     = url;
  a.download = `warehouse_report_${Date.now()}.txt`;
  a.click();
  URL.revokeObjectURL(url);
  showToast('📄 Report exported successfully.');
}

/* ═══════════════════════════════════════════
   DARK / LIGHT MODE TOGGLE
═══════════════════════════════════════════ */
function toggleDarkMode() {
  const body = document.body;
  const isDark = body.getAttribute('data-theme') === 'dark';
  body.setAttribute('data-theme', isDark ? 'light' : 'dark');
  document.getElementById('darkModeToggle').textContent = isDark ? '☀️' : '🌙';
  // Rebuild chart with updated colours
  setTimeout(renderChart, 50);
}

/* ═══════════════════════════════════════════
   TOAST NOTIFICATION
═══════════════════════════════════════════ */
let toastTimer = null;

function showToast(message, type = 'success') {
  const toast = document.getElementById('toast');
  toast.textContent = message;
  toast.className   = `toast ${type}`;
  toast.classList.remove('hidden');

  if (toastTimer) clearTimeout(toastTimer);
  toastTimer = setTimeout(() => {
    toast.classList.add('hidden');
  }, 3500);
}
