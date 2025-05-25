document.addEventListener('DOMContentLoaded', function() {
    // Load books and users when page loads
    loadBooks();
    loadUsers();
    
    // Set up form event listeners
    document.getElementById('book-form').addEventListener('submit', addBook);
    document.getElementById('user-form').addEventListener('submit', addUser);
    document.getElementById('checkout-form').addEventListener('submit', checkoutBook);
    document.getElementById('return-form').addEventListener('submit', returnBook);
});

function openTab(tabName) {
    // Hide all tab contents
    const tabContents = document.getElementsByClassName('tab-content');
    for (let i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = 'none';
    }
    
    // Remove active class from all buttons
    const tabButtons = document.getElementsByClassName('tab-button');
    for (let i = 0; i < tabButtons.length; i++) {
        tabButtons[i].classList.remove('active');
    }
    
    // Show the current tab and mark button as active
    document.getElementById(tabName).style.display = 'block';
    event.currentTarget.classList.add('active');
}

function showAddBookForm() {
    document.getElementById('add-book-form').style.display = 'block';
}

function showAddUserForm() {
    document.getElementById('add-user-form').style.display = 'block';
}

function loadBooks() {
    fetch('http://localhost:8000/api/books')
        .then(response => response.json())
        .then(books => {
            const tableBody = document.getElementById('books-table-body');
            tableBody.innerHTML = '';
            
            books.forEach(book => {
                const row = document.createElement('tr');
                
                const titleCell = document.createElement('td');
                titleCell.textContent = book.title;
                
                const authorCell = document.createElement('td');
                authorCell.textContent = book.author;
                
                const isbnCell = document.createElement('td');
                isbnCell.textContent = book.isbn;
                
                const availableCell = document.createElement('td');
                availableCell.textContent = `${book.availableCopies}/${book.totalCopies}`;
                
                const actionsCell = document.createElement('td');
                const removeButton = document.createElement('button');
                removeButton.textContent = 'Remove';
                removeButton.onclick = () => removeBook(book.isbn);
                actionsCell.appendChild(removeButton);
                
                row.appendChild(titleCell);
                row.appendChild(authorCell);
                row.appendChild(isbnCell);
                row.appendChild(availableCell);
                row.appendChild(actionsCell);
                
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading books:', error));
}

function loadUsers() {
    fetch('http://localhost:8000/api/users')
        .then(response => response.json())
        .then(users => {
            const tableBody = document.getElementById('users-table-body');
            tableBody.innerHTML = '';
            
            users.forEach(user => {
                const row = document.createElement('tr');
                
                const nameCell = document.createElement('td');
                nameCell.textContent = user.name;
                
                const idCell = document.createElement('td');
                idCell.textContent = user.userId;
                
                const typeCell = document.createElement('td');
                typeCell.textContent = user.userType;
                
                row.appendChild(nameCell);
                row.appendChild(idCell);
                row.appendChild(typeCell);
                
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading users:', error));
}

function addBook(event) {
    event.preventDefault();
    
    const title = document.getElementById('book-title').value;
    const author = document.getElementById('book-author').value;
    const isbn = document.getElementById('book-isbn').value;
    const copies = document.getElementById('book-copies').value;
    
    fetch('http://localhost:8000/api/books', {
        method: 'POST',
        body: `${title},${author},${isbn},${copies}`
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadBooks();
        document.getElementById('book-form').reset();
        document.getElementById('add-book-form').style.display = 'none';
    })
    .catch(error => console.error('Error adding book:', error));
}

function addUser(event) {
    event.preventDefault();
    
    const name = document.getElementById('user-name').value;
    const userId = document.getElementById('user-id').value;
    const userType = document.getElementById('user-type').value;
    
    fetch('http://localhost:8000/api/users', {
        method: 'POST',
        body: `${name},${userId},${userType}`
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadUsers();
        document.getElementById('user-form').reset();
        document.getElementById('add-user-form').style.display = 'none';
    })
    .catch(error => console.error('Error adding user:', error));
}

function removeBook(isbn) {
    if (confirm('Are you sure you want to remove this book?')) {
        fetch(`http://localhost:8000/api/books`, {
            method: 'DELETE',
            body: isbn
        })
        .then(response => response.text())
        .then(message => {
            alert(message);
            loadBooks();
        })
        .catch(error => console.error('Error removing book:', error));
    }
}

function checkoutBook(event) {
    event.preventDefault();
    
    const isbn = document.getElementById('checkout-isbn').value;
    const userId = document.getElementById('checkout-user').value;
    
    fetch('http://localhost:8000/api/checkout', {
        method: 'POST',
        body: `${isbn},${userId}`
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        document.getElementById('checkout-form').reset();
        loadBooks();
    })
    .catch(error => console.error('Error checking out book:', error));
}

function returnBook(event) {
    event.preventDefault();
    
    const isbn = document.getElementById('return-isbn').value;
    const userId = document.getElementById('return-user').value;
    
    fetch('http://localhost:8000/api/return', {
        method: 'POST',
        body: `${isbn},${userId}`
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        document.getElementById('return-form').reset();
        loadBooks();
    })
    .catch(error => console.error('Error returning book:', error));
}
