/**
 * Regata Online - JavaScript Principal
 * Funcionalidades interactivas para la aplicación
 */

// ===== CONFIGURACIÓN GLOBAL =====
const RegataApp = {
    config: {
        alertAutoHideDelay: 5000,
        animationDuration: 300,
        debounceDelay: 300
    },
    
    // Cache de elementos DOM
    elements: {},
    
    // Estado de la aplicación
    state: {
        currentView: 'card',
        searchFilters: {},
        sortOrder: 'asc'
    }
};

// ===== INICIALIZACIÓN =====
document.addEventListener('DOMContentLoaded', function() {
    RegataApp.init();
});

RegataApp.init = function() {
    this.cacheElements();
    this.bindEvents();
    this.initializeComponents();
    this.setupAlerts();
    this.setupFormValidation();
    this.setupSearchFilters();
    this.setupViewToggle();
    this.setupRangeSliders();
    this.setupPreviewUpdates();
    
    console.log('Regata Online App initialized successfully');
};

// ===== CACHE DE ELEMENTOS DOM =====
RegataApp.cacheElements = function() {
    this.elements = {
        // Alertas
        alerts: document.querySelectorAll('.alert'),
        
        // Formularios
        forms: document.querySelectorAll('form'),
        rangeSliders: document.querySelectorAll('input[type="range"]'),
        
        // Búsqueda y filtros
        searchInput: document.getElementById('busqueda'),
        filterButtons: document.querySelectorAll('.filter-btn'),
        clearFiltersBtn: document.getElementById('limpiarFiltros'),
        
        // Vista
        viewToggleButtons: document.querySelectorAll('.view-toggle'),
        cardView: document.getElementById('vistaCards'),
        tableView: document.getElementById('vistaTabla'),
        
        // Modales
        deleteModals: document.querySelectorAll('.modal'),
        
        // Previews
        previewSections: document.querySelectorAll('.preview-section'),
        
        // Botones de acción
        actionButtons: document.querySelectorAll('[data-action]')
    };
};

// ===== EVENTOS =====
RegataApp.bindEvents = function() {
    // Eventos de búsqueda
    if (this.elements.searchInput) {
        this.elements.searchInput.addEventListener('input', 
            this.debounce(this.handleSearch.bind(this), this.config.debounceDelay)
        );
    }
    
    // Eventos de filtros
    this.elements.filterButtons.forEach(btn => {
        btn.addEventListener('click', this.handleFilterClick.bind(this));
    });
    
    if (this.elements.clearFiltersBtn) {
        this.elements.clearFiltersBtn.addEventListener('click', this.clearAllFilters.bind(this));
    }
    
    // Eventos de vista
    this.elements.viewToggleButtons.forEach(btn => {
        btn.addEventListener('click', this.handleViewToggle.bind(this));
    });
    
    // Eventos de range sliders
    this.elements.rangeSliders.forEach(slider => {
        slider.addEventListener('input', this.handleRangeSliderChange.bind(this));
    });
    
    // Eventos de formularios
    this.elements.forms.forEach(form => {
        form.addEventListener('submit', this.handleFormSubmit.bind(this));
    });
    
    // Eventos de botones de acción
    this.elements.actionButtons.forEach(btn => {
        btn.addEventListener('click', this.handleActionClick.bind(this));
    });
    
    // Eventos globales
    window.addEventListener('resize', this.debounce(this.handleResize.bind(this), 250));
};

// ===== COMPONENTES =====
RegataApp.initializeComponents = function() {
    // Inicializar tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Inicializar popovers de Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Aplicar animaciones de entrada
    this.applyFadeInAnimations();
};

// ===== ALERTAS =====
RegataApp.setupAlerts = function() {
    this.elements.alerts.forEach(alert => {
        // Auto-hide alerts
        setTimeout(() => {
            this.hideAlert(alert);
        }, this.config.alertAutoHideDelay);
        
        // Agregar funcionalidad de cierre manual
        const closeBtn = alert.querySelector('.btn-close');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => this.hideAlert(alert));
        }
    });
};

RegataApp.hideAlert = function(alert) {
    if (alert && alert.classList.contains('show')) {
        alert.classList.remove('show');
        alert.classList.add('fade');
        
        setTimeout(() => {
            if (alert.parentNode) {
                alert.parentNode.removeChild(alert);
            }
        }, this.config.animationDuration);
    }
};

RegataApp.showAlert = function(message, type = 'info') {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    const container = document.querySelector('.container');
    if (container) {
        container.insertAdjacentHTML('afterbegin', alertHtml);
        
        // Auto-hide después del delay configurado
        const newAlert = container.querySelector('.alert');
        setTimeout(() => {
            this.hideAlert(newAlert);
        }, this.config.alertAutoHideDelay);
    }
};

// ===== VALIDACIÓN DE FORMULARIOS =====
RegataApp.setupFormValidation = function() {
    this.elements.forms.forEach(form => {
        // Validación en tiempo real
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', () => this.validateField(input));
            input.addEventListener('input', () => this.clearFieldError(input));
        });
    });
};

RegataApp.validateField = function(field) {
    const value = field.value.trim();
    const fieldName = field.name;
    let isValid = true;
    let errorMessage = '';
    
    // Validaciones específicas por tipo de campo
    switch (fieldName) {
        case 'nombre':
            if (value.length < 2) {
                isValid = false;
                errorMessage = 'El nombre debe tener al menos 2 caracteres';
            }
            break;
            
        case 'email':
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (value && !emailRegex.test(value)) {
                isValid = false;
                errorMessage = 'Ingrese un email válido';
            }
            break;
            
        case 'telefono':
            const phoneRegex = /^[\d\s\-\+\(\)]+$/;
            if (value && !phoneRegex.test(value)) {
                isValid = false;
                errorMessage = 'Ingrese un teléfono válido';
            }
            break;
            
        case 'velocidadMaxima':
        case 'resistencia':
        case 'maniobrabilidad':
        case 'capacidadCombustible':
            const numValue = parseInt(value);
            if (numValue < 1 || numValue > 100) {
                isValid = false;
                errorMessage = 'El valor debe estar entre 1 y 100';
            }
            break;
    }
    
    // Mostrar/ocultar error
    if (!isValid) {
        this.showFieldError(field, errorMessage);
    } else {
        this.clearFieldError(field);
    }
    
    return isValid;
};

RegataApp.showFieldError = function(field, message) {
    this.clearFieldError(field);
    
    field.classList.add('is-invalid');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
};

RegataApp.clearFieldError = function(field) {
    field.classList.remove('is-invalid');
    
    const errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
};

// ===== BÚSQUEDA Y FILTROS =====
RegataApp.setupSearchFilters = function() {
    // Inicializar estado de filtros
    this.state.searchFilters = {
        search: '',
        filters: new Set()
    };
};

RegataApp.handleSearch = function(event) {
    const searchTerm = event.target.value.toLowerCase().trim();
    this.state.searchFilters.search = searchTerm;
    this.applyFilters();
};

RegataApp.handleFilterClick = function(event) {
    event.preventDefault();
    const button = event.currentTarget;
    const filterValue = button.dataset.filter;
    
    // Toggle filter
    if (this.state.searchFilters.filters.has(filterValue)) {
        this.state.searchFilters.filters.delete(filterValue);
        button.classList.remove('active');
    } else {
        this.state.searchFilters.filters.add(filterValue);
        button.classList.add('active');
    }
    
    this.applyFilters();
};

RegataApp.clearAllFilters = function() {
    // Limpiar búsqueda
    if (this.elements.searchInput) {
        this.elements.searchInput.value = '';
    }
    
    // Limpiar filtros
    this.state.searchFilters.search = '';
    this.state.searchFilters.filters.clear();
    
    // Remover clases activas
    this.elements.filterButtons.forEach(btn => {
        btn.classList.remove('active');
    });
    
    this.applyFilters();
};

RegataApp.applyFilters = function() {
    const items = document.querySelectorAll('.filterable-item');
    let visibleCount = 0;
    
    items.forEach(item => {
        const shouldShow = this.shouldShowItem(item);
        
        if (shouldShow) {
            item.style.display = '';
            item.classList.add('fade-in');
            visibleCount++;
        } else {
            item.style.display = 'none';
            item.classList.remove('fade-in');
        }
    });
    
    // Actualizar contador de resultados
    this.updateResultsCounter(visibleCount);
};

RegataApp.shouldShowItem = function(item) {
    const searchTerm = this.state.searchFilters.search;
    const activeFilters = this.state.searchFilters.filters;
    
    // Verificar búsqueda de texto
    if (searchTerm) {
        const itemText = item.textContent.toLowerCase();
        if (!itemText.includes(searchTerm)) {
            return false;
        }
    }
    
    // Verificar filtros activos
    if (activeFilters.size > 0) {
        const itemFilters = new Set(item.dataset.filters ? item.dataset.filters.split(',') : []);
        const hasMatchingFilter = [...activeFilters].some(filter => itemFilters.has(filter));
        if (!hasMatchingFilter) {
            return false;
        }
    }
    
    return true;
};

RegataApp.updateResultsCounter = function(count) {
    const counter = document.getElementById('resultados-contador');
    if (counter) {
        counter.textContent = `${count} resultado${count !== 1 ? 's' : ''}`;
    }
};

// ===== VISTA (CARDS/TABLA) =====
RegataApp.setupViewToggle = function() {
    // Establecer vista inicial
    const savedView = localStorage.getItem('regata-view-preference') || 'card';
    this.setView(savedView);
};

RegataApp.handleViewToggle = function(event) {
    event.preventDefault();
    const button = event.currentTarget;
    const view = button.dataset.view;
    
    this.setView(view);
    
    // Guardar preferencia
    localStorage.setItem('regata-view-preference', view);
};

RegataApp.setView = function(view) {
    this.state.currentView = view;
    
    // Actualizar botones
    this.elements.viewToggleButtons.forEach(btn => {
        btn.classList.toggle('active', btn.dataset.view === view);
    });
    
    // Mostrar/ocultar vistas
    if (this.elements.cardView) {
        this.elements.cardView.style.display = view === 'card' ? '' : 'none';
    }
    
    if (this.elements.tableView) {
        this.elements.tableView.style.display = view === 'table' ? '' : 'none';
    }
};

// ===== RANGE SLIDERS =====
RegataApp.setupRangeSliders = function() {
    this.elements.rangeSliders.forEach(slider => {
        this.updateRangeSliderDisplay(slider);
    });
};

RegataApp.handleRangeSliderChange = function(event) {
    const slider = event.target;
    this.updateRangeSliderDisplay(slider);
    this.updatePreview();
};

RegataApp.updateRangeSliderDisplay = function(slider) {
    const value = slider.value;
    const output = document.getElementById(slider.id + 'Value');
    
    if (output) {
        output.textContent = value;
    }
    
    // Actualizar color del slider basado en el valor
    const percentage = ((value - slider.min) / (slider.max - slider.min)) * 100;
    slider.style.background = `linear-gradient(to right, var(--primary-color) 0%, var(--primary-color) ${percentage}%, var(--bg-secondary) ${percentage}%, var(--bg-secondary) 100%)`;
};

// ===== PREVIEW EN TIEMPO REAL =====
RegataApp.setupPreviewUpdates = function() {
    const previewInputs = document.querySelectorAll('[data-preview]');
    previewInputs.forEach(input => {
        input.addEventListener('input', this.updatePreview.bind(this));
        input.addEventListener('change', this.updatePreview.bind(this));
    });
    
    // Actualización inicial
    this.updatePreview();
};

RegataApp.updatePreview = function() {
    // Preview para jugadores
    this.updatePlayerPreview();
    
    // Preview para modelos
    this.updateModelPreview();
    
    // Preview para barcos
    this.updateBoatPreview();
};

RegataApp.updatePlayerPreview = function() {
    const previewSection = document.getElementById('preview-jugador');
    if (!previewSection) return;
    
    const nombre = document.getElementById('nombre')?.value || 'Nuevo Jugador';
    const email = document.getElementById('email')?.value || 'email@ejemplo.com';
    const telefono = document.getElementById('telefono')?.value || 'No especificado';
    const experiencia = document.getElementById('experiencia')?.value || '0';
    const victorias = document.getElementById('victorias')?.value || '0';
    
    previewSection.innerHTML = `
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${nombre}</h5>
                <p class="card-text">
                    <i class="bi bi-envelope"></i> ${email}<br>
                    <i class="bi bi-telephone"></i> ${telefono}
                </p>
                <div class="row text-center">
                    <div class="col-6">
                        <div class="h4 text-info">${experiencia}</div>
                        <small class="text-muted">Experiencia</small>
                    </div>
                    <div class="col-6">
                        <div class="h4 text-success">${victorias}</div>
                        <small class="text-muted">Victorias</small>
                    </div>
                </div>
            </div>
        </div>
    `;
};

RegataApp.updateModelPreview = function() {
    const previewSection = document.getElementById('preview-modelo');
    if (!previewSection) return;
    
    const nombre = document.getElementById('nombre')?.value || 'Nuevo Modelo';
    const tipo = document.getElementById('tipo')?.value || 'Deportivo';
    const velocidad = document.getElementById('velocidadMaxima')?.value || '50';
    const resistencia = document.getElementById('resistencia')?.value || '50';
    const maniobrabilidad = document.getElementById('maniobrabilidad')?.value || '50';
    const combustible = document.getElementById('capacidadCombustible')?.value || '50';
    
    const puntuacionTotal = Math.round((parseInt(velocidad) + parseInt(resistencia) + parseInt(maniobrabilidad) + parseInt(combustible)) / 4);
    
    previewSection.innerHTML = `
        <div class="card">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <div>
                        <h5 class="card-title">${nombre}</h5>
                        <span class="badge bg-primary">${tipo}</span>
                    </div>
                    <div class="text-end">
                        <div class="h4 text-warning">${puntuacionTotal}</div>
                        <small class="text-muted">Puntuación</small>
                    </div>
                </div>
                <div class="row">
                    <div class="col-6 mb-2">
                        <small class="text-muted">Velocidad</small>
                        <div class="progress" style="height: 15px;">
                            <div class="progress-bar bg-info" style="width: ${velocidad}%"></div>
                        </div>
                    </div>
                    <div class="col-6 mb-2">
                        <small class="text-muted">Resistencia</small>
                        <div class="progress" style="height: 15px;">
                            <div class="progress-bar bg-success" style="width: ${resistencia}%"></div>
                        </div>
                    </div>
                    <div class="col-6 mb-2">
                        <small class="text-muted">Maniobrabilidad</small>
                        <div class="progress" style="height: 15px;">
                            <div class="progress-bar bg-warning" style="width: ${maniobrabilidad}%"></div>
                        </div>
                    </div>
                    <div class="col-6 mb-2">
                        <small class="text-muted">Combustible</small>
                        <div class="progress" style="height: 15px;">
                            <div class="progress-bar bg-danger" style="width: ${combustible}%"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
};

RegataApp.updateBoatPreview = function() {
    const previewSection = document.getElementById('preview-barco');
    if (!previewSection) return;
    
    const nombre = document.getElementById('nombre')?.value || 'Nuevo Barco';
    const color = document.getElementById('color')?.value || '#007bff';
    const jugadorSelect = document.getElementById('jugador');
    const modeloSelect = document.getElementById('modelo');
    
    const jugadorNombre = jugadorSelect?.options[jugadorSelect.selectedIndex]?.text || 'Seleccionar jugador';
    const modeloNombre = modeloSelect?.options[modeloSelect.selectedIndex]?.text || 'Seleccionar modelo';
    
    previewSection.innerHTML = `
        <div class="card">
            <div class="card-body">
                <div class="d-flex align-items-center mb-3">
                    <i class="bi bi-sailboat display-6 me-3" style="color: ${color}"></i>
                    <div>
                        <h5 class="card-title mb-1">${nombre}</h5>
                        <span class="color-badge" style="background-color: ${color}">${color}</span>
                    </div>
                </div>
                <div class="mb-2">
                    <small class="text-muted">Propietario:</small>
                    <div>${jugadorNombre}</div>
                </div>
                <div class="mb-2">
                    <small class="text-muted">Modelo:</small>
                    <div>${modeloNombre}</div>
                </div>
            </div>
        </div>
    `;
};

// ===== MANEJO DE FORMULARIOS =====
RegataApp.handleFormSubmit = function(event) {
    const form = event.target;
    
    // Validar todos los campos
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!this.validateField(input)) {
            isValid = false;
        }
    });
    
    if (!isValid) {
        event.preventDefault();
        this.showAlert('Por favor, corrija los errores en el formulario', 'danger');
        return false;
    }
    
    // Mostrar indicador de carga
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) {
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Procesando...';
        submitBtn.disabled = true;
        
        // Restaurar después de un tiempo (en caso de error)
        setTimeout(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }, 10000);
    }
};

// ===== MANEJO DE ACCIONES =====
RegataApp.handleActionClick = function(event) {
    const button = event.currentTarget;
    const action = button.dataset.action;
    
    switch (action) {
        case 'delete':
            this.confirmDelete(button);
            break;
        case 'activate':
        case 'deactivate':
            this.confirmStatusChange(button, action);
            break;
        case 'repair':
        case 'refuel':
            this.confirmMaintenance(button, action);
            break;
    }
};

RegataApp.confirmDelete = function(button) {
    const itemName = button.dataset.itemName || 'este elemento';
    const confirmMessage = `¿Estás seguro de que deseas eliminar ${itemName}? Esta acción no se puede deshacer.`;
    
    if (confirm(confirmMessage)) {
        // Proceder con la eliminación
        const form = button.closest('form');
        if (form) {
            form.submit();
        }
    }
};

RegataApp.confirmStatusChange = function(button, action) {
    const itemName = button.dataset.itemName || 'este elemento';
    const actionText = action === 'activate' ? 'activar' : 'desactivar';
    const confirmMessage = `¿Deseas ${actionText} ${itemName}?`;
    
    if (confirm(confirmMessage)) {
        const form = button.closest('form');
        if (form) {
            form.submit();
        }
    }
};

RegataApp.confirmMaintenance = function(button, action) {
    const actionText = action === 'repair' ? 'reparar' : 'recargar combustible de';
    const itemName = button.dataset.itemName || 'este barco';
    const confirmMessage = `¿Deseas ${actionText} ${itemName}?`;
    
    if (confirm(confirmMessage)) {
        const form = button.closest('form');
        if (form) {
            form.submit();
        }
    }
};

// ===== ANIMACIONES =====
RegataApp.applyFadeInAnimations = function() {
    const elements = document.querySelectorAll('.card, .table, .alert');
    elements.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            element.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * 100);
    });
};

// ===== RESPONSIVE =====
RegataApp.handleResize = function() {
    // Ajustar vista en dispositivos móviles
    if (window.innerWidth < 768) {
        this.setView('card');
    }
    
    // Recalcular alturas si es necesario
    this.adjustHeights();
};

RegataApp.adjustHeights = function() {
    const cards = document.querySelectorAll('.card-view .card');
    if (cards.length > 0) {
        // Resetear alturas
        cards.forEach(card => {
            card.style.height = 'auto';
        });
        
        // Igualar alturas en filas (solo en desktop)
        if (window.innerWidth >= 768) {
            let maxHeight = 0;
            cards.forEach(card => {
                maxHeight = Math.max(maxHeight, card.offsetHeight);
            });
            
            cards.forEach(card => {
                card.style.height = maxHeight + 'px';
            });
        }
    }
};

// ===== UTILIDADES =====
RegataApp.debounce = function(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
};

RegataApp.formatNumber = function(num) {
    return new Intl.NumberFormat('es-ES').format(num);
};

RegataApp.formatDate = function(date) {
    return new Intl.DateTimeFormat('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(new Date(date));
};

// ===== FUNCIONES ESPECÍFICAS PARA LISTA DE MODELOS =====
RegataApp.setupModelListView = function() {
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
    
    // View toggle functionality
    const cardView = document.getElementById('cardView');
    const tableView = document.getElementById('tableView');
    const cardContainer = document.getElementById('cardContainer');
    const tableContainer = document.getElementById('tableContainer');
    
    if (cardView && tableView && cardContainer && tableContainer) {
        cardView.addEventListener('click', function() {
            cardView.classList.add('active');
            tableView.classList.remove('active');
            cardContainer.classList.remove('d-none');
            tableContainer.classList.add('d-none');
        });
        
        tableView.addEventListener('click', function() {
            tableView.classList.add('active');
            cardView.classList.remove('active');
            tableContainer.classList.remove('d-none');
            cardContainer.classList.add('d-none');
        });
    }
};

// ===== FUNCIONES GLOBALES PARA COMPATIBILIDAD =====
window.confirmarEliminacion = function(modeloId, modeloNombre) {
    if (modeloId && modeloNombre) {
        document.getElementById('modeloNombre').textContent = modeloNombre;
        document.getElementById('deleteForm').action = '/modelos/' + modeloId + '/eliminar';
        new bootstrap.Modal(document.getElementById('deleteModal')).show();
    } else {
        return confirm('¿Estás seguro de que deseas eliminar este elemento? Esta acción no se puede deshacer.');
    }
};

window.mostrarAlerta = function(mensaje, tipo = 'info') {
    RegataApp.showAlert(mensaje, tipo);
};

window.actualizarPreview = function() {
    RegataApp.updatePreview();
};

// Inicializar funciones específicas de lista de modelos si estamos en esa página
document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('cardView') || document.getElementById('tableView')) {
        RegataApp.setupModelListView();
    }
});

// ===== EXPORTAR PARA USO GLOBAL =====
window.RegataApp = RegataApp;