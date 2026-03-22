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