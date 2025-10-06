// AngularJS SPA Application
var app = angular.module('regataApp', []);

// Main Controller
app.controller('MainController', function($scope, $http) {
    // User data (temporal - será reemplazado por autenticación real)
    $scope.user = {
        name: 'Administrador',
        role: 'ADMIN' // Cambiar a 'JUGADOR' para probar acceso restringido
    };
    
    // Current section
    $scope.currentSection = 'home';
    
    // Show section function
    $scope.showSection = function(section) {
        // Verificar permisos
        if (section === 'usuarios' && $scope.user.role !== 'ADMIN') {
            alert('Solo los administradores pueden acceder a la gestión de usuarios');
            return;
        }
        
        $scope.currentSection = section;
        console.log('Navegando a:', section);
    };
    
    // Cambiar rol de usuario (para testing)
    $scope.toggleUserRole = function() {
        $scope.user.role = $scope.user.role === 'ADMIN' ? 'JUGADOR' : 'ADMIN';
        $scope.user.name = $scope.user.role === 'ADMIN' ? 'Administrador' : 'Jugador';
        console.log('Rol cambiado a:', $scope.user.role);
    };
});
