document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('criterioForm');
    const criterioInput = document.getElementById('criterio');
    const criterioTableBody = document.getElementById('criterioTableBody');

    let criterios = [];

    form.addEventListener('submit', (event) => {
        event.preventDefault();
        const criterio = criterioInput.value.trim();
        if (criterio) {
            addCriterio(criterio);
            criterioInput.value = '';
        }
    });

    function addCriterio(criterio) {
        criterios.push(criterio);
        updateTable();
    }

    function updateTable() {
        criterioTableBody.innerHTML = '';
        criterios.forEach((criterio, index) => {
            const row = document.createElement('tr');
            const criterioCell = document.createElement('td');
            criterioCell.textContent = criterio;
            row.appendChild(criterioCell);

            const actionsCell = document.createElement('td');
            const editButton = document.createElement('button');
            editButton.textContent = 'Editar';
            editButton.addEventListener('click', () => editCriterio(index));
            actionsCell.appendChild(editButton);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Excluir';
            deleteButton.addEventListener('click', () => deleteCriterio(index));
            actionsCell.appendChild(deleteButton);

            row.appendChild(actionsCell);
            criterioTableBody.appendChild(row);
        });
    }

    function editCriterio(index) {
        const newCriterio = prompt('Editar Critério:', criterios[index]);
        if (newCriterio !== null) {
            criterios[index] = newCriterio.trim();
            updateTable();
        }
    }

    function deleteCriterio(index) {
        criterios.splice(index, 1);
        updateTable();
    }
});
