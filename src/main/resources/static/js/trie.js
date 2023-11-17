function filterOperations(type) {
    var rows = document.querySelectorAll('tr.operation-row');
    rows.forEach(function(row) {
        row.style.display = '';
        if (type === 'deposit' && !row.classList.contains('deposit')) {
            row.style.display = 'none';
        } else if (type === 'withdrawal' && !row.classList.contains('withdrawal')) {
            row.style.display = 'none';
        }
    });
}
