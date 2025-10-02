// Regata Online - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    console.log('\n' + '🌟'.repeat(60));
    console.log('🚀 REGATA ONLINE - JAVASCRIPT INICIADO');
    console.log('🌟'.repeat(60));
    console.log('📅 Timestamp:', new Date().toISOString());
    console.log('🌐 URL actual:', window.location.href);
    console.log('📄 Título de página:', document.title);
    console.log('🔍 User Agent:', navigator.userAgent);
    console.log('📱 Viewport:', `${window.innerWidth}x${window.innerHeight}`);
    
    console.log('🎯 ANÁLISIS DEL DOM:');
    console.log('   ├── Total elementos:', document.querySelectorAll('*').length);
    console.log('   ├── Formularios:', document.querySelectorAll('form').length);
    console.log('   ├── Botones:', document.querySelectorAll('button').length);
    console.log('   ├── Inputs:', document.querySelectorAll('input').length);
    console.log('   └── Links:', document.querySelectorAll('a').length);
    
    // Initialize tooltips
    console.log('🔧 INICIALIZANDO TOOLTIPS...');
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    console.log(`   └── Tooltips encontrados: ${tooltipTriggerList.length}`);
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    console.log('🔧 INICIALIZANDO POPOVERS...');
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    console.log(`   └── Popovers encontrados: ${popoverTriggerList.length}`);
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Search form enhancement
    const searchForms = document.querySelectorAll('form[action*="buscar"]');
    searchForms.forEach(function(form) {
        const input = form.querySelector('input[type="text"]');
        if (input) {
            input.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    form.submit();
                }
            });
        }
    });

    // Table row click enhancement
    const tableRows = document.querySelectorAll('table tbody tr[data-href]');
    tableRows.forEach(function(row) {
        row.style.cursor = 'pointer';
        row.addEventListener('click', function() {
            window.location.href = this.dataset.href;
        });
    });

    // Confirmation dialogs
    const deleteButtons = document.querySelectorAll('a[onclick*="confirm"]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            const message = this.getAttribute('onclick').match(/confirm\('([^']+)'\)/);
            if (message && !confirm(message[1])) {
                e.preventDefault();
            }
        });
    });

    // Dynamic form updates
    const modelSelect = document.getElementById('modelo');
    const velocidadInput = document.getElementById('velocidadActual');
    const resistenciaInput = document.getElementById('resistenciaActual');
    const maniobrabilidadInput = document.getElementById('maniobrabilidadActual');

    if (modelSelect && velocidadInput && resistenciaInput && maniobrabilidadInput) {
        modelSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if (selectedOption.value) {
                // Extract stats from option text (format: "Modelo (Vel: X, Res: Y, Man: Z)")
                const text = selectedOption.text;
                const velMatch = text.match(/Vel: (\d+)/);
                const resMatch = text.match(/Res: (\d+)/);
                const manMatch = text.match(/Man: (\d+)/);
                
                if (velMatch) velocidadInput.placeholder = velMatch[1];
                if (resMatch) resistenciaInput.placeholder = resMatch[1];
                if (manMatch) maniobrabilidadInput.placeholder = manMatch[1];
            }
        });
    }

    // Statistics preview update
    const statsInputs = document.querySelectorAll('#velocidadMaxima, #resistencia, #maniobrabilidad');
    const statsDisplays = document.querySelectorAll('.card h3');
    
    statsInputs.forEach(function(input, index) {
        input.addEventListener('input', function() {
            if (statsDisplays[index]) {
                statsDisplays[index].textContent = this.value + (index === 0 ? ' km/h' : ' pts');
            }
        });
    });

    // SUPER DETAILED BUTTON CLICK LOGGING
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    console.log('🔘 INICIALIZANDO LOGS DE BOTONES SUBMIT');
    console.log(`📊 Botones submit encontrados: ${submitButtons.length}`);
    
    submitButtons.forEach(function(button, index) {
        console.log(`   └── Botón ${index + 1}: "${button.textContent.trim()}" (ID: ${button.id || 'SIN ID'})`);
        
        button.addEventListener('click', function(e) {
            console.log('\n' + '🔘'.repeat(50));
            console.log('🖱️  CLICK EN BOTÓN SUBMIT DETECTADO');
            console.log('🔘'.repeat(50));
            console.log('📅 Timestamp click:', new Date().toISOString());
            console.log('🎯 INFORMACIÓN DEL BOTÓN:');
            console.log('   ├── Texto del botón:', this.textContent.trim());
            console.log('   ├── ID del botón:', this.id || 'SIN ID');
            console.log('   ├── Clases CSS:', this.className || 'SIN CLASES');
            console.log('   ├── Tipo:', this.type);
            console.log('   ├── Deshabilitado:', this.disabled);
            console.log('   └── Posición en DOM:', Array.from(this.parentNode.children).indexOf(this));
            
            const form = this.closest('form');
            console.log('🔍 ANÁLISIS DEL FORMULARIO ASOCIADO:');
            if (form) {
                console.log('   ├── Formulario encontrado: ✅');
                console.log('   ├── ID del formulario:', form.id || 'SIN ID');
                console.log('   ├── Action:', form.action);
                console.log('   ├── Method:', form.method);
                console.log('   ├── Válido HTML5:', form.checkValidity());
                console.log('   └── Número de campos:', form.elements.length);
                
                // Análisis detallado de validez
                const invalidFields = form.querySelectorAll(':invalid');
                console.log('🚨 CAMPOS INVÁLIDOS:');
                if (invalidFields.length > 0) {
                    console.log(`   ├── Total campos inválidos: ${invalidFields.length}`);
                    invalidFields.forEach((field, idx) => {
                        console.log(`   ├── Campo inválido ${idx + 1}:`);
                        console.log(`   │   ├── Nombre: ${field.name}`);
                        console.log(`   │   ├── Valor: "${field.value}"`);
                        console.log(`   │   ├── Mensaje validación: ${field.validationMessage}`);
                        console.log(`   │   └── Tipo error: ${field.validity.valueMissing ? 'REQUERIDO' : field.validity.typeMismatch ? 'TIPO INCORRECTO' : 'OTRO'}`);
                    });
                } else {
                    console.log('   └── ✅ Todos los campos son válidos');
                }
                
                if (form.checkValidity()) {
                    console.log('✅ FORMULARIO VÁLIDO - APLICANDO ESTADO DE CARGA');
                    const originalText = this.innerHTML;
                    console.log('   ├── Texto original guardado:', originalText);
                    this.innerHTML = '<span class="loading"></span> Procesando...';
                    this.disabled = true;
                    console.log('   ├── Botón deshabilitado: ✅');
                    console.log('   └── Texto cambiado a "Procesando...": ✅');
                    
                    // Re-enable button after 10 seconds as fallback
                    setTimeout(() => {
                        console.log('⏰ TIMEOUT DE SEGURIDAD ACTIVADO (10s)');
                        console.log('   ├── Restaurando texto original...');
                        this.innerHTML = originalText;
                        this.disabled = false;
                        console.log('   └── Botón rehabilitado por timeout');
                    }, 10000);
                } else {
                    console.log('❌ FORMULARIO INVÁLIDO - NO SE APLICA ESTADO DE CARGA');
                    console.log('   └── El usuario debe corregir los errores primero');
                }
            } else {
                console.log('   └── ❌ NO SE ENCONTRÓ FORMULARIO ASOCIADO');
            }
            
            console.log('🔘'.repeat(50) + '\n');
        });
    });

    // Enhanced form validation for CRUD forms with SUPER DETAILED LOGGING
    const crudForms = document.querySelectorAll('#formCrearJugador, #formEditarJugador, #formCrearModelo, #formEditarModelo, #formCrearBarco, #formEditarBarco');
    console.log('🔍 INICIALIZANDO LOGS DETALLADOS DE FORMULARIOS');
    console.log('📋 Formularios CRUD encontrados:', crudForms.length);
    crudForms.forEach((form, index) => {
        console.log(`   └── Formulario ${index + 1}: ${form.id || 'SIN ID'} (${form.tagName})`);
    });
    
    crudForms.forEach(function(form) {
        console.log(`🎯 CONFIGURANDO LISTENERS PARA: ${form.id}`);
        
        form.addEventListener('submit', function(e) {
            console.log('\n' + '🚀'.repeat(50));
            console.log('🔥 EVENTO SUBMIT DETECTADO EN FRONTEND');
            console.log('🚀'.repeat(50));
            console.log('📅 Timestamp:', new Date().toISOString());
            console.log('🎯 INFORMACIÓN DEL FORMULARIO:');
            console.log('   ├── ID del formulario:', this.id || 'SIN ID');
            console.log('   ├── Clase CSS:', this.className || 'SIN CLASE');
            console.log('   ├── Action URL:', this.action || 'SIN ACTION');
            console.log('   ├── Método HTTP:', this.method || 'GET');
            console.log('   ├── Encoding:', this.enctype || 'application/x-www-form-urlencoded');
            console.log('   └── Número de campos:', this.elements.length);
            
            console.log('📊 ANÁLISIS DETALLADO DE CAMPOS:');
            const formData = new FormData(this);
            let fieldCount = 0;
            for (let [key, value] of formData.entries()) {
                fieldCount++;
                const field = this.querySelector(`[name="${key}"]`);
                console.log(`   ├── Campo ${fieldCount}:`);
                console.log(`   │   ├── Nombre: "${key}"`);
                console.log(`   │   ├── Valor: "${value}" (length: ${value.length})`);
                console.log(`   │   ├── Tipo: ${field ? field.type : 'DESCONOCIDO'}`);
                console.log(`   │   ├── Requerido: ${field ? field.required : 'DESCONOCIDO'}`);
                console.log(`   │   └── Válido: ${field ? field.checkValidity() : 'DESCONOCIDO'}`);
            }
            console.log(`   └── Total campos con datos: ${fieldCount}`);
            
            console.log('🔍 VALIDACIÓN FRONTEND:');
            const requiredFields = this.querySelectorAll('[required]');
            console.log(`   ├── Campos requeridos encontrados: ${requiredFields.length}`);
            let isValid = true;
            let invalidFields = [];
            
            requiredFields.forEach(function(field, index) {
                const isEmpty = !field.value.trim();
                console.log(`   ├── Campo requerido ${index + 1}: "${field.name}"`);
                console.log(`   │   ├── Valor: "${field.value}"`);
                console.log(`   │   ├── Vacío: ${isEmpty}`);
                console.log(`   │   └── Válido HTML5: ${field.checkValidity()}`);
                
                if (isEmpty) {
                    invalidFields.push(field.name);
                    field.classList.add('is-invalid');
                    isValid = false;
                } else {
                    field.classList.remove('is-invalid');
                    field.classList.add('is-valid');
                }
            });
            
            console.log(`   └── Resultado validación: ${isValid ? '✅ VÁLIDO' : '❌ INVÁLIDO'}`);
            if (!isValid) {
                console.log(`   └── Campos inválidos: [${invalidFields.join(', ')}]`);
            }
            
            if (!isValid) {
                console.log('🚫 CANCELANDO ENVÍO - FORMULARIO INVÁLIDO');
                console.log('🚀'.repeat(50) + '\n');
                e.preventDefault();
                return false;
            }
            
            console.log('✅ FORMULARIO VÁLIDO - PROCEDIENDO CON ENVÍO');
            console.log('🌐 ENVIANDO REQUEST HTTP...');
            console.log('🚀'.repeat(50) + '\n');
        });
        
        // Real-time validation
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(function(input) {
            input.addEventListener('blur', function() {
                if (this.hasAttribute('required') && !this.value.trim()) {
                    this.classList.add('is-invalid');
                    this.classList.remove('is-valid');
                } else if (this.value.trim()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                }
            });
            
            input.addEventListener('input', function() {
                if (this.classList.contains('is-invalid') && this.value.trim()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                }
            });
        });
    });

    // Smooth scrolling for anchor links
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    anchorLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Table sorting (simple client-side)
    const sortableHeaders = document.querySelectorAll('th[data-sort]');
    sortableHeaders.forEach(function(header) {
        header.style.cursor = 'pointer';
        header.addEventListener('click', function() {
            const table = this.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            const columnIndex = Array.from(this.parentNode.children).indexOf(this);
            const isAscending = this.classList.contains('sort-asc');
            
            // Remove sort classes from all headers
            sortableHeaders.forEach(h => {
                h.classList.remove('sort-asc', 'sort-desc');
            });
            
            // Add sort class to current header
            this.classList.add(isAscending ? 'sort-desc' : 'sort-asc');
            
            // Sort rows
            rows.sort(function(a, b) {
                const aText = a.children[columnIndex].textContent.trim();
                const bText = b.children[columnIndex].textContent.trim();
                
                // Try to parse as numbers
                const aNum = parseFloat(aText);
                const bNum = parseFloat(bText);
                
                if (!isNaN(aNum) && !isNaN(bNum)) {
                    return isAscending ? bNum - aNum : aNum - bNum;
                } else {
                    return isAscending ? bText.localeCompare(aText) : aText.localeCompare(bText);
                }
            });
            
            // Reorder rows in table
            rows.forEach(row => tbody.appendChild(row));
        });
    });

    // Auto-refresh for statistics (if needed)
    const refreshInterval = 30000; // 30 seconds
    const autoRefreshElements = document.querySelectorAll('[data-auto-refresh]');
    if (autoRefreshElements.length > 0) {
        setInterval(function() {
            autoRefreshElements.forEach(function(element) {
                const url = element.dataset.autoRefresh;
                if (url) {
                    fetch(url)
                        .then(response => response.text())
                        .then(html => {
                            element.innerHTML = html;
                        })
                        .catch(error => console.log('Auto-refresh failed:', error));
                }
            });
        }, refreshInterval);
    }

    // Print functionality
    const printButtons = document.querySelectorAll('[data-print]');
    printButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            window.print();
        });
    });

    // Export functionality (placeholder)
    const exportButtons = document.querySelectorAll('[data-export]');
    exportButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const format = this.dataset.export;
            const table = this.closest('.card').querySelector('table');
            
            if (table) {
                if (format === 'csv') {
                    exportTableToCSV(table, 'data.csv');
                } else if (format === 'json') {
                    exportTableToJSON(table, 'data.json');
                }
            }
        });
    });

    // Utility functions
    function exportTableToCSV(table, filename) {
        const rows = table.querySelectorAll('tr');
        const csv = Array.from(rows).map(row => {
            const cells = row.querySelectorAll('td, th');
            return Array.from(cells).map(cell => `"${cell.textContent.trim()}"`).join(',');
        }).join('\n');
        
        const blob = new Blob([csv], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    }

    function exportTableToJSON(table, filename) {
        const rows = table.querySelectorAll('tr');
        const headers = Array.from(rows[0].querySelectorAll('th')).map(th => th.textContent.trim());
        const data = Array.from(rows).slice(1).map(row => {
            const cells = Array.from(row.querySelectorAll('td'));
            const obj = {};
            headers.forEach((header, index) => {
                obj[header] = cells[index] ? cells[index].textContent.trim() : '';
            });
            return obj;
        });
        
        const json = JSON.stringify(data, null, 2);
        const blob = new Blob([json], { type: 'application/json' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    }

    // Initialize animations
    const animatedElements = document.querySelectorAll('.fade-in, .slide-in');
    animatedElements.forEach(function(element) {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        
        const observer = new IntersectionObserver(function(entries) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                    entry.target.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
                }
            });
        });
        
        observer.observe(element);
    });

    console.log('✅ INICIALIZACIÓN COMPLETADA EXITOSAMENTE');
    console.log('🌟'.repeat(60));
    console.log('🎉 REGATA ONLINE - LISTO PARA DEBUGGING');
    console.log('🌟'.repeat(60));
    console.log('📝 INSTRUCCIONES DE DEBUGGING:');
    console.log('   ├── Abre las DevTools (F12)');
    console.log('   ├── Ve a la pestaña "Console"');
    console.log('   ├── Haz click en cualquier botón de formulario');
    console.log('   ├── Observa los logs detallados con emojis');
    console.log('   └── Reporta cualquier error o comportamiento extraño');
    console.log('🌟'.repeat(60) + '\n');
});
