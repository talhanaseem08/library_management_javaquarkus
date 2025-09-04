// API Configuration
const API_BASE_URL = 'http://localhost:8080';

// Global state
let currentLendingTab = 'active';
let books = [];
let members = [];
let lendings = [];

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    loadDashboardData();
});

// Initialize application
function initializeApp() {
    // Set default section
    showSection('dashboard');
    
    // Load initial data
    loadBooks();
    loadMembers();
    loadLendings();
}

// Setup event listeners
function setupEventListeners() {
    // Mobile menu toggle
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');
    
    hamburger.addEventListener('click', () => {
        hamburger.classList.toggle('active');
        navMenu.classList.toggle('active');
    });

    // Search functionality
    const bookSearch = document.getElementById('book-search');
    const memberSearch = document.getElementById('member-search');
    
    if (bookSearch) {
        bookSearch.addEventListener('input', (e) => filterBooks(e.target.value));
    }
    
    if (memberSearch) {
        memberSearch.addEventListener('input', (e) => filterMembers(e.target.value));
    }

    // Form submissions
    const addBookForm = document.getElementById('add-book-form');
    const addMemberForm = document.getElementById('add-member-form');
    const lendBookForm = document.getElementById('lend-book-form');
    
    if (addBookForm) {
        addBookForm.addEventListener('submit', handleAddBook);
    }
    
    if (addMemberForm) {
        addMemberForm.addEventListener('submit', handleAddMember);
    }
    
    if (lendBookForm) {
        lendBookForm.addEventListener('submit', handleLendBook);
    }
}

// Navigation functions
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(sectionId).classList.add('active');
    
    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    document.querySelector(`[onclick="showSection('${sectionId}')"]`).classList.add('active');
    
    // Load section-specific data
    switch(sectionId) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'books':
            loadBooks();
            break;
        case 'members':
            loadMembers();
            break;
        case 'lending':
            loadLendings();
            break;
    }
}

function showLendingTab(tab) {
    currentLendingTab = tab;
    
    // Update tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    event.target.classList.add('active');
    
    // Reload lendings with current tab
    loadLendings();
}

// Modal functions
function showAddBookModal() {
    document.getElementById('add-book-modal').style.display = 'block';
}

function showAddMemberModal() {
    document.getElementById('add-member-modal').style.display = 'block';
}

function showLendBookModal() {
    // Populate book and member dropdowns
    populateBookDropdown();
    populateMemberDropdown();
    document.getElementById('lend-book-modal').style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    
    // Reset forms
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
        form.reset();
    }
}

// API Functions
async function apiCall(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    
    try {
        const response = await fetch(url, { ...defaultOptions, ...options });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || `HTTP ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        showToast(error.message, 'error');
        throw error;
    }
}

// Data loading functions
async function loadBooks() {
    try {
        showLoading('books-table-body');
        books = await apiCall('/books');
        renderBooksTable();
        updateDashboardStats();
    } catch (error) {
        showEmptyState('books-table-body', 'No books found', 'Add your first book to get started');
    }
}

async function loadMembers() {
    try {
        showLoading('members-table-body');
        members = await apiCall('/members');
        renderMembersTable();
        updateDashboardStats();
    } catch (error) {
        showEmptyState('members-table-body', 'No members found', 'Add your first member to get started');
    }
}

async function loadLendings() {
    try {
        showLoading('lendings-table-body');
        lendings = await apiCall('/lending/history');
        renderLendingsTable();
        updateDashboardStats();
        updateRecentActivity();
    } catch (error) {
        showEmptyState('lendings-table-body', 'No lending history', 'Start lending books to see activity');
    }
}

async function loadDashboardData() {
    await Promise.all([loadBooks(), loadMembers(), loadLendings()]);
}

// Rendering functions
function renderBooksTable() {
    const tbody = document.getElementById('books-table-body');
    
    if (books.length === 0) {
        showEmptyState('books-table-body', 'No books found', 'Add your first book to get started');
        return;
    }
    
    tbody.innerHTML = books.map(book => `
        <tr>
            <td><strong>${book.title}</strong></td>
            <td>${book.author}</td>
            <td>
                <span class="quantity-badge">${book.quantity}</span>
            </td>
            <td>
                <span class="status-badge ${book.available ? 'status-available' : 'status-unavailable'}">
                    ${book.available ? 'Available' : 'Unavailable'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-secondary btn-sm" onclick="editBook('${book.id}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteBook('${book.id}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function renderMembersTable() {
    const tbody = document.getElementById('members-table-body');
    
    if (members.length === 0) {
        showEmptyState('members-table-body', 'No members found', 'Add your first member to get started');
        return;
    }
    
    tbody.innerHTML = members.map(member => `
        <tr>
            <td><strong>${member.name}</strong></td>
            <td>${member.email}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-secondary btn-sm" onclick="editMember('${member.id}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteMember('${member.id}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function renderLendingsTable() {
    const tbody = document.getElementById('lendings-table-body');
    
    if (lendings.length === 0) {
        showEmptyState('lendings-table-body', 'No lending history', 'Start lending books to see activity');
        return;
    }
    
    // Filter based on current tab
    const filteredLendings = currentLendingTab === 'active' 
        ? lendings.filter(lending => !lending.returnDate)
        : lendings;
    
    if (filteredLendings.length === 0) {
        const message = currentLendingTab === 'active' 
            ? 'No active lendings' 
            : 'No lending history';
        showEmptyState('lendings-table-body', message, 'Start lending books to see activity');
        return;
    }
    
    tbody.innerHTML = filteredLendings.map(lending => {
        const book = books.find(b => b.id === lending.bookId);
        const member = members.find(m => m.id === lending.memberId);
        const isReturned = lending.returnDate;
        
        return `
            <tr>
                <td><strong>${book ? book.title : 'Unknown Book'}</strong></td>
                <td>${member ? member.name : 'Unknown Member'}</td>
                <td>${formatDate(lending.lendingDate)}</td>
                <td>${lending.returnDate ? formatDate(lending.returnDate) : '-'}</td>
                <td>
                    <span class="status-badge ${isReturned ? 'status-returned' : 'status-lent'}">
                        ${isReturned ? 'Returned' : 'Lent'}
                    </span>
                </td>
                <td>
                    ${!isReturned ? `
                        <button class="btn btn-success btn-sm" onclick="returnBook('${lending.lendingId}')">
                            <i class="fas fa-undo"></i> Return
                        </button>
                    ` : ''}
                </td>
            </tr>
        `;
    }).join('');
}

// Form handlers
async function handleAddBook(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const bookData = {
        title: formData.get('title'),
        author: formData.get('author')
    };
    
    try {
        await apiCall('/books', {
            method: 'POST',
            body: JSON.stringify(bookData)
        });
        
        showToast('Book added successfully!', 'success');
        closeModal('add-book-modal');
        loadBooks();
    } catch (error) {
        // Error already shown by apiCall
    }
}

async function handleAddMember(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const memberData = {
        name: formData.get('name'),
        email: formData.get('email')
    };
    
    try {
        await apiCall('/members', {
            method: 'POST',
            body: JSON.stringify(memberData)
        });
        
        showToast('Member added successfully!', 'success');
        closeModal('add-member-modal');
        loadMembers();
    } catch (error) {
        // Error already shown by apiCall
    }
}

async function handleLendBook(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const lendingData = {
        bookId: formData.get('bookId'),
        memberId: formData.get('memberId')
    };
    
    try {
        await apiCall('/lending', {
            method: 'POST',
            body: JSON.stringify(lendingData)
        });
        
        showToast('Book lent successfully!', 'success');
        closeModal('lend-book-modal');
        loadLendings();
        loadBooks(); // Refresh books to update quantity
    } catch (error) {
        // Error already shown by apiCall
    }
}

// Action functions
async function returnBook(lendingId) {
    try {
        await apiCall(`/lending/returns/${lendingId}`, {
            method: 'POST'
        });
        
        showToast('Book returned successfully!', 'success');
        loadLendings();
        loadBooks(); // Refresh books to update quantity
    } catch (error) {
        // Error already shown by apiCall
    }
}

async function deleteBook(bookId) {
    if (!confirm('Are you sure you want to delete this book?')) {
        return;
    }
    
    try {
        await apiCall(`/books/${bookId}`, {
            method: 'DELETE'
        });
        
        showToast('Book deleted successfully!', 'success');
        loadBooks();
    } catch (error) {
        // Error already shown by apiCall
    }
}

async function deleteMember(memberId) {
    if (!confirm('Are you sure you want to delete this member?')) {
        return;
    }
    
    try {
        await apiCall(`/members/${memberId}`, {
            method: 'DELETE'
        });
        
        showToast('Member deleted successfully!', 'success');
        loadMembers();
    } catch (error) {
        // Error already shown by apiCall
    }
}

// Utility functions
function updateDashboardStats() {
    const totalBooks = books.length;
    const totalMembers = members.length;
    const activeLendings = lendings.filter(l => !l.returnDate).length;
    const availableBooks = books.filter(b => b.available).length;
    
    document.getElementById('total-books').textContent = totalBooks;
    document.getElementById('total-members').textContent = totalMembers;
    document.getElementById('active-lendings').textContent = activeLendings;
    document.getElementById('available-books').textContent = availableBooks;
}

function updateRecentActivity() {
    const recentLendings = lendings
        .sort((a, b) => new Date(b.lendingDate) - new Date(a.lendingDate))
        .slice(0, 5);
    
    const container = document.getElementById('recent-lendings');
    
    if (recentLendings.length === 0) {
        container.innerHTML = '<p class="empty-state">No recent activity</p>';
        return;
    }
    
    container.innerHTML = recentLendings.map(lending => {
        const book = books.find(b => b.id === lending.bookId);
        const member = members.find(m => m.id === lending.memberId);
        const isReturned = lending.returnDate;
        
        return `
            <div class="activity-item">
                <div class="activity-icon">
                    <i class="fas ${isReturned ? 'fa-undo' : 'fa-book'}"></i>
                </div>
                <div class="activity-content">
                    <h4>${isReturned ? 'Book Returned' : 'Book Lent'}</h4>
                    <p>${book ? book.title : 'Unknown Book'} - ${member ? member.name : 'Unknown Member'}</p>
                    <small>${formatDate(lending.lendingDate)}</small>
                </div>
            </div>
        `;
    }).join('');
}

function populateBookDropdown() {
    const select = document.getElementById('lend-book-select');
    const availableBooks = books.filter(book => book.available);
    
    select.innerHTML = '<option value="">Choose a book...</option>' +
        availableBooks.map(book => 
            `<option value="${book.id}">${book.title} by ${book.author} (${book.quantity} available)</option>`
        ).join('');
}

function populateMemberDropdown() {
    const select = document.getElementById('lend-member-select');
    
    select.innerHTML = '<option value="">Choose a member...</option>' +
        members.map(member => 
            `<option value="${member.id}">${member.name} (${member.email})</option>`
        ).join('');
}

function filterBooks(searchTerm) {
    const filteredBooks = books.filter(book => 
        book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.author.toLowerCase().includes(searchTerm.toLowerCase())
    );
    
    renderFilteredBooks(filteredBooks);
}

function filterMembers(searchTerm) {
    const filteredMembers = members.filter(member => 
        member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        member.email.toLowerCase().includes(searchTerm.toLowerCase())
    );
    
    renderFilteredMembers(filteredMembers);
}

function renderFilteredBooks(filteredBooks) {
    const tbody = document.getElementById('books-table-body');
    
    if (filteredBooks.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">No books found matching your search</td></tr>';
        return;
    }
    
    tbody.innerHTML = filteredBooks.map(book => `
        <tr>
            <td><strong>${book.title}</strong></td>
            <td>${book.author}</td>
            <td>
                <span class="quantity-badge">${book.quantity}</span>
            </td>
            <td>
                <span class="status-badge ${book.available ? 'status-available' : 'status-unavailable'}">
                    ${book.available ? 'Available' : 'Unavailable'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-secondary btn-sm" onclick="editBook('${book.id}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteBook('${book.id}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function renderFilteredMembers(filteredMembers) {
    const tbody = document.getElementById('members-table-body');
    
    if (filteredMembers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="empty-state">No members found matching your search</td></tr>';
        return;
    }
    
    tbody.innerHTML = filteredMembers.map(member => `
        <tr>
            <td><strong>${member.name}</strong></td>
            <td>${member.email}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-secondary btn-sm" onclick="editMember('${member.id}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteMember('${member.id}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// UI Helper functions
function showLoading(containerId) {
    document.getElementById(containerId).innerHTML = '<tr><td colspan="5" class="loading">Loading...</td></tr>';
}

function showEmptyState(containerId, title, message) {
    document.getElementById(containerId).innerHTML = `
        <tr>
            <td colspan="5" class="empty-state">
                <i class="fas fa-inbox"></i>
                <h3>${title}</h3>
                <p>${message}</p>
            </td>
        </tr>
    `;
}

function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icon = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
    }[type];
    
    toast.innerHTML = `
        <i class="${icon}"></i>
        <span>${message}</span>
    `;
    
    toastContainer.appendChild(toast);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        toast.remove();
    }, 5000);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}

// Close modals when clicking outside
window.onclick = function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// Close modals with Escape key
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            if (modal.style.display === 'block') {
                modal.style.display = 'none';
            }
        });
    }
});
