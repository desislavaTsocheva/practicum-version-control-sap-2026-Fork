function toggleFolder(element) {
    const folderHeader = element.parentElement;
    const nestedDocs = folderHeader.nextElementSibling;
    if (nestedDocs && nestedDocs.classList.contains('nested-docs')) {
        const isVisible = nestedDocs.style.display === 'block';
        nestedDocs.style.display = isVisible ? 'none' : 'block';
    }
}
function handleFileClick(element) {
    const id = element.getAttribute('data-id');
    const name = element.getAttribute('data-name');
    const description = element.getAttribute('data-description');
    const date = element.getAttribute('data-date');
    showDetails(name, description, date, id);
}

function showDetails(name, description, date, id) {
    console.log("Switching to file ID:", id);

    const title = document.getElementById('selected-file-title');
    if (title) title.innerText = name;

    const container = document.getElementById('active-version-container');
    if (container) {
        container.innerHTML = `
        <div class="version-item" style="display: flex; justify-content: space-between; align-items: center; padding: 15px; border: 1px solid #eee; border-radius: 8px;">
            <div class="file-info" style="display: flex; align-items: center; gap: 12px;">
                <img src="${iconDocPath}" alt="doc" style="width: 25px;">
                <div>
                    <strong style="display: block;">${name}</strong>
                    <small style="color: #888;">${description} | </small>
                    <small style="color: #888;">${date}</small>
                </div>
            </div>
            <div style="text-align: right;">
                <a href="/document-microservice/documents/download/${id}">
                   <img src="${iconDownloadPath}"
                   class="file-icon" alt="download" style="width: 30px; height: 30px; object-fit: contain;">
                </a>
            </div>
        </div>
    `;
    }
}

function showProjectFields() {
    const fields = document.getElementById('projectFields');
    const mainBtn = document.getElementById('toggleProjectBtn');
    if (fields.style.display === 'flex') {
        fields.style.display = 'none';
        mainBtn.innerHTML = '<span>NEW PROJECT</span>';

        mainBtn.style.backgroundColor = '#90B69F';
        mainBtn.style.color = '#000';
        mainBtn.style.border = '1px solid #90B69F';
    } else {
        fields.style.display = 'flex';
        mainBtn.innerHTML = '<span>Cancel</span>';
        mainBtn.style.backgroundColor = '#f3caca';
        mainBtn.style.color = '#b31c1c';
        mainBtn.style.border = '1px solid #b31c1c';
    }
}