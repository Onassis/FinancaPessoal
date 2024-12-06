document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('CadastroForm');
    const criterioAdd = document.getElementById('criterioAdd');
    const input_criterio = document.getElementById('criterio');
    const criterioTableBody = document.getElementById('criterioTable');
    const btnAddCriterio = document.getElementById('btnAddCriterio');

    const submit = document.getElementById("btnSalvar");
	submit.addEventListener("click", validate);

    let criterios = generateArray();
    updateTable();
  
    
    function clickTabela() { 		
	     document.querySelectorAll('#criterioTable td').forEach(td => {
            td.addEventListener('click', function() {
                const largura = td.offsetWidth;
                const altura = td.offsetHeight;
                const valorAtual = td.innerText;
                const input = document.createElement('input');
                input.type = 'text';
                input.value = valorAtual;
                input.style.width = `${largura}px`;
                input.style.height = `${altura}px`;

                input.addEventListener('blur', function() {
                    td.innerText = input.value;
                });

                td.innerText = '';
                td.appendChild(input);
                input.focus();
            });
        })
        };
	function validate(e) {
	  const criterioField = document.getElementById("criterio");
	  let valid = true;
	
	  if (!criterioField.value) {
		e.preventDefault(); 
		console.log("erro de criterio");
	  	const criterioAdd = document.getElementById("criterioAdd");
		criterioAdd.classList.add("invalid");
	    const nameError = document.getElementById("nameError");
	    nameError.classList.add("visible");
	    firstNameField.classList.add("invalid");
	    nameError.setAttribute("aria-hidden", false);
	    nameError.setAttribute("aria-invalid", true);
	    
	  }
	  return valid;
	}

    function generateArray() {
        // Pega o valor do input
        const inputText = input_criterio.value;
        return inputText.split(';').map(item => item.trim());
    } 
    function arrayToString(array) {
        // Junta os elementos do array em uma string, separados por vírgulas
        return array.join('; ');
    }
   
    btnAddCriterio.addEventListener ('click', (event) => { 
        event.preventDefault();
        
		btnAddCriterio.classList.remove("invalid");
		const nameError = document.getElementById("nameError");
	    nameError.classList.remove("visible");
        addCriterio()
    });                
 

    function addCriterio() {
        
        console.log("add Criterio");
        console.log( criterioAdd.value.trim());
        const criterio = criterioAdd.value.trim();
        if (criterio) {
         criterios.push(criterio);
         criterioAdd.value = '';
         updateTable(); 
        } 
    }
    function criaCabecalho() { 
		// Cria o elemento do cabeçalho
    	var thead = document.createElement('thead');

  	  
    	// Cria uma linha para o cabeçalho
    	var linha = document.createElement('tr');
    
    	// Cria e adiciona a coluna "Critério"
    	var colunaCriterio = document.createElement('th');
    	colunaCriterio.textContent = 'Critério';
    	linha.appendChild(colunaCriterio);
    
    	// Cria e adiciona a coluna "Ação"
    	var colunaAcao = document.createElement('th');
    	colunaAcao.textContent = 'Ação';
    	linha.appendChild(colunaAcao);
    
    	// Adiciona a linha ao cabeçalho
    	thead.appendChild(linha);
    	
    	// Adiciona o cabeçalho à tabela
    	criterioTableBody.appendChild(thead);
    
	}
    function updateTable() {
        criterios.sort((a, b) => a.toLowerCase().localeCompare(b.toLowerCase()));
        const resultString = arrayToString(criterios);
        input_criterio.value = resultString; 

        criterioTableBody.innerHTML = '';
        criaCabecalho();

        criterios.forEach((criterio, index) => {
            const row = document.createElement('tr');
            const criterioCell = document.createElement('td');
  			criterioCell.textContent = criterio;
//  			criterioCell.width = '500px';
  			criterioCell.width = '80%';
  			
            criterioCell.addEventListener('click', (event)  => editCriterio(event,index));

          
            row.appendChild(criterioCell);
		
            const actionsCell = document.createElement('td');
            actionsCell.width = '20%';
              
            const editButton = document.createElement('a');
            //editButton.textContent = 'Editar';
            // Add multiple classes
			editButton.classList.add("fa-regular","fa-pen-to-square","icon-dark");  
            editButton.addEventListener('click', (event)  => editCriterio(event,index));
            actionsCell.appendChild(editButton);

            const deleteButton = document.createElement('a');
            //deleteButton.textContent = 'Excluir';
			deleteButton.classList.add("fa-regular","fa-trash-can","icon-dark");  
            
            deleteButton.addEventListener('click', (event)  => deleteCriterio(event,index));
            actionsCell.appendChild(deleteButton);

            row.appendChild(actionsCell);
            criterioTableBody.appendChild(row);
        });
          
    }

 
function editCriterio(event,index) {
    event.preventDefault();
    // Seleciona a linha correspondente
    const row = criterioTableBody.rows[index+1];
    const criterioCell = row.cells[0]; // Primeira célula, que contém o critério

    // Salva o valor original do critério
    const originalCriterio = criterios[index];
    
    const teste =  document.querySelectorAll('#criterioTable td');
     
    
    // Obtém as dimensões da célula
    const cellWidth = teste.offsetWidth ; 
    const cellHeight = teste.offsetHeight;

    // Cria um input para edição
    const input = document.createElement('input');
    input.type = 'text';
    input.value = originalCriterio;
    input.className = 'edit-input';
      // Ajusta o tamanho do input para coincidir com o da célula
    //input.width = `${cellWidth}px`;
    //input.height = `${cellHeight}px`;
   input.style.width = '100%'
   //'500px';
    
    input.style.boxSizing = 'border-box'; // Garante que padding/borda sejam inclusos
    
    // Substitui o conteúdo da célula pelo input
    criterioCell.innerHTML = '';
    criterioCell.appendChild(input);
    input.focus();

    // Adiciona eventos para confirmar ou cancelar a edição
    input.addEventListener('blur', () => saveEdit());
    input.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            saveEdit();
        } else if (event.key === 'Escape') {
            cancelEdit();
        }
    });

    function saveEdit() {
        const newCriterio = input.value.trim();
        if (newCriterio) {
            criterios[index] = newCriterio;
        } else {
            cancelEdit();
        }
        updateTable();
    }

    function cancelEdit() {
        criterioCell.innerHTML = originalCriterio;
    }
}


    function deleteCriterio(event,index) {
        event.preventDefault();        
        criterios.splice(index, 1);
        updateTable();
    }
});
