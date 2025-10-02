// Regata Online - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
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

    // Loading states for forms
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            if (form && form.checkValidity()) {
                this.innerHTML = '<span class="loading"></span> Procesando...';
                this.disabled = true;
            }
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

    console.log('Regata Online - JavaScript initialized successfully');
});
