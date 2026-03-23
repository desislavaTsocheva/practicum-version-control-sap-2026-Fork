function deleteUser(userId) {
    if (confirm("Are you sure you want to delete this user?")) {
        const token = localStorage.getItem('token');
        fetch(`/auth-microservice/admin/users/delete/${userId}`, {
            method: 'DELETE',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(res => {
            if (res.ok) location.reload();
            else alert("Action failed!");
        });
    }
}

function editUser(userId) {
    window.location.href = `/auth-microservice/admin/users/edit/${userId}`;
}

function openTab(evt, tabName) {
    const tabContents = document.getElementsByClassName("tab-content");
    for (let i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = "none";
    }
    const tabBtns = document.getElementsByClassName("tab-btn");
    for (let i = 0; i < tabBtns.length; i++) {
        tabBtns[i].className = tabBtns[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}