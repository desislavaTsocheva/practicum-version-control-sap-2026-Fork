function openTab(evt, tabName) {
    const tabContents = document.getElementsByClassName("tab-content");
    for (let i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = "none";
        tabContents[i].classList.remove("active-content");
    }
    const tabBtns = document.getElementsByClassName("tab-btn");
    for (let i = 0; i < tabBtns.length; i++) {
        tabBtns[i].className = tabBtns[i].className.replace(" active", "");
    }
    const selectedTab = document.getElementById(tabName);
    if (selectedTab) {
        selectedTab.style.display = "block";
        selectedTab.classList.add("active-content");
    }
    evt.currentTarget.className += " active";
}

function filterTable(inputId, tableId) {
    const input = document.getElementById(inputId);
    const filter = input.value.toLowerCase();
    const table = document.getElementById(tableId);
    if (!table) return;

    const rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) {
        let rowVisible = false;
        const cells = rows[i].getElementsByTagName("td");

        for (let j = 0; j < cells.length; j++) {
            const cell = cells[j];
            if (cell) {
                const textValue = cell.textContent || cell.innerText;
                if (textValue.toLowerCase().indexOf(filter) > -1) {
                    rowVisible = true;
                    break;
                }
            }
        }
        rows[i].style.display = rowVisible ? "" : "none";
    }
}

function filterUsers() {
    const input = document.getElementById('userSearchInput');
    const filter = input.value.toLowerCase();
    const rows = document.querySelectorAll('.user-row');

    rows.forEach(row => {
        const username = row.querySelector('.col-user')?.textContent.toLowerCase() || "";
        const email = row.querySelector('.col-email')?.textContent.toLowerCase() || "";

        if (username.includes(filter) || email.includes(filter)) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

function deleteUser(userId) {
    if (confirm("Are you sure you want to delete this user?")) {
        const token = localStorage.getItem('token');
        fetch(`/auth-microservice/admin/users/delete/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }).then(res => {
            if (res.ok) {
                showToast("User deleted successfully!");
                setTimeout(() => location.reload(), 1000);
            } else {
                showToast("Failed to delete user.", "error");
            }
        });
    }
}

function editUser(userId) {
    const btn = document.querySelector(`button[data-id="${userId}"]`) ||
        document.querySelector(`button[th\\:data-id="${userId}"]`);

    if (btn) {
        const row = btn.closest('tr');
        const username = row.querySelector('.col-user span').textContent;

        document.getElementById('editUserId').value = userId;
        document.getElementById('modalUsername').textContent = "User: " + username;
        document.getElementById('editUserModal').style.display = "block";
    }
}

function closeModal() {
    document.getElementById('editUserModal').style.display = "none";
}

document.addEventListener('DOMContentLoaded', function() {
    const editForm = document.getElementById('editRoleForm');
    if (editForm) {
        editForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const userId = document.getElementById('editUserId').value;
            const newRole = document.getElementById('newRole').value;
            const token = localStorage.getItem('token');

            fetch(`/auth-microservice/users/update-role/${userId}?role=${newRole}`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (response.ok) {
                    showToast("Role updated successfully!");
                    closeModal();
                    setTimeout(() => location.reload(), 1000);
                } else {
                    showToast("Error updating role.", "error");
                }
            }).catch(err => console.error("Fetch Error:", err));
        });
    }

    const userSearchInput = document.getElementById('userSearchInput');
    if (userSearchInput) {
        userSearchInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                filterUsers();
            }
        });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('editUserModal');
        if (event.target === modal) {
            closeModal();
        }
    };
});

function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span>${message}</span>
        <button onclick="this.parentElement.remove()" style="background:none; border:none; color:white; cursor:pointer; font-size:1.2rem; margin-left:10px;">&times;</button>
    `;

    container.appendChild(toast);
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 500);
    }, 3000);
}