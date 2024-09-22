document.addEventListener('DOMContentLoaded', () => {
    const checkboxes = document.querySelectorAll('.status-checkbox');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const row = checkbox.closest('tr');
            const statusCell = row.querySelector('td:nth-child(5)'); // Ajuste o índice se necessário

            if (checkbox.checked) {
                statusCell.textContent = 'Ativo';
                statusCell.style.color = 'green'; // Opcional: altera a cor do texto para verde
            } else {
                statusCell.textContent = 'Desativado';
                statusCell.style.color = 'red'; // Opcional: altera a cor do texto para vermelho
            }
        });
    });
});
