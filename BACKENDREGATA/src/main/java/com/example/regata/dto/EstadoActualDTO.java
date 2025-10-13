package com.example.regata.dto;

import com.example.regata.model.Participacion;
import com.example.regata.model.Movimiento;

/**
 * DTO que combina información de PARTICIPACION (capa de negocio) 
 * con el último MOVIMIENTO (estado físico actual)
 */
public class EstadoActualDTO {
    
    // Información de PARTICIPACION (meta-información del juego)
    private Long idParticipacion;
    private Participacion.Estado estado;
    private Integer ordenTurno;
    private Long partidaId;
    private String partidaNombre;
    private Long barcoId;
    private String barcoAlias;
    private Long jugadorId;
    private String jugadorNombre;
    private Long celdaInicialId;
    private String celdaInicialInfo;
    
    // Información del último MOVIMIENTO (estado físico actual)
    private Integer turnoActual;
    private Integer posX;
    private Integer posY;
    private Integer velX;
    private Integer velY;
    private Integer deltaVx;
    private Integer deltaVy;
    private Movimiento.Resultado ultimoResultado;
    
    // Información adicional
    private Long totalMovimientos;
    private Boolean esGanador;
    
    // Constructores
    public EstadoActualDTO() {}
    
    public EstadoActualDTO(Participacion participacion, Movimiento ultimoMovimiento) {
        // Información de la participación
        this.idParticipacion = participacion.getIdParticipacion();
        this.estado = participacion.getEstado();
        this.ordenTurno = participacion.getOrdenTurno();
        this.partidaId = participacion.getPartida().getIdPartida();
        this.partidaNombre = participacion.getPartida().getMapa().getNombre(); // Asumiendo que el mapa tiene nombre
        this.barcoId = participacion.getBarco().getIdBarco();
        this.barcoAlias = participacion.getBarco().getAlias();
        this.jugadorId = participacion.getJugador().getIdUsuario();
        this.jugadorNombre = participacion.getJugador().getNombre();
        this.celdaInicialId = participacion.getCeldaInicial().getIdCelda();
        this.celdaInicialInfo = "(" + participacion.getCeldaInicial().getCoordX() + "," + 
                               participacion.getCeldaInicial().getCoordY() + ")";
        
        // Información del último movimiento
        if (ultimoMovimiento != null) {
            this.turnoActual = ultimoMovimiento.getTurno();
            this.posX = ultimoMovimiento.getPosX();
            this.posY = ultimoMovimiento.getPosY();
            this.velX = ultimoMovimiento.getVelX();
            this.velY = ultimoMovimiento.getVelY();
            this.deltaVx = ultimoMovimiento.getDeltaVx();
            this.deltaVy = ultimoMovimiento.getDeltaVy();
            this.ultimoResultado = ultimoMovimiento.getResultado();
        }
    }
    
    // Getters y Setters
    public Long getIdParticipacion() {
        return idParticipacion;
    }
    
    public void setIdParticipacion(Long idParticipacion) {
        this.idParticipacion = idParticipacion;
    }
    
    public Participacion.Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Participacion.Estado estado) {
        this.estado = estado;
    }
    
    public Integer getOrdenTurno() {
        return ordenTurno;
    }
    
    public void setOrdenTurno(Integer ordenTurno) {
        this.ordenTurno = ordenTurno;
    }
    
    public Long getPartidaId() {
        return partidaId;
    }
    
    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }
    
    public String getPartidaNombre() {
        return partidaNombre;
    }
    
    public void setPartidaNombre(String partidaNombre) {
        this.partidaNombre = partidaNombre;
    }
    
    public Long getBarcoId() {
        return barcoId;
    }
    
    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }
    
    public String getBarcoAlias() {
        return barcoAlias;
    }
    
    public void setBarcoAlias(String barcoAlias) {
        this.barcoAlias = barcoAlias;
    }
    
    public Long getJugadorId() {
        return jugadorId;
    }
    
    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }
    
    public String getJugadorNombre() {
        return jugadorNombre;
    }
    
    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
    }
    
    public Long getCeldaInicialId() {
        return celdaInicialId;
    }
    
    public void setCeldaInicialId(Long celdaInicialId) {
        this.celdaInicialId = celdaInicialId;
    }
    
    public String getCeldaInicialInfo() {
        return celdaInicialInfo;
    }
    
    public void setCeldaInicialInfo(String celdaInicialInfo) {
        this.celdaInicialInfo = celdaInicialInfo;
    }
    
    public Integer getTurnoActual() {
        return turnoActual;
    }
    
    public void setTurnoActual(Integer turnoActual) {
        this.turnoActual = turnoActual;
    }
    
    public Integer getPosX() {
        return posX;
    }
    
    public void setPosX(Integer posX) {
        this.posX = posX;
    }
    
    public Integer getPosY() {
        return posY;
    }
    
    public void setPosY(Integer posY) {
        this.posY = posY;
    }
    
    public Integer getVelX() {
        return velX;
    }
    
    public void setVelX(Integer velX) {
        this.velX = velX;
    }
    
    public Integer getVelY() {
        return velY;
    }
    
    public void setVelY(Integer velY) {
        this.velY = velY;
    }
    
    public Integer getDeltaVx() {
        return deltaVx;
    }
    
    public void setDeltaVx(Integer deltaVx) {
        this.deltaVx = deltaVx;
    }
    
    public Integer getDeltaVy() {
        return deltaVy;
    }
    
    public void setDeltaVy(Integer deltaVy) {
        this.deltaVy = deltaVy;
    }
    
    public Movimiento.Resultado getUltimoResultado() {
        return ultimoResultado;
    }
    
    public void setUltimoResultado(Movimiento.Resultado ultimoResultado) {
        this.ultimoResultado = ultimoResultado;
    }
    
    public Long getTotalMovimientos() {
        return totalMovimientos;
    }
    
    public void setTotalMovimientos(Long totalMovimientos) {
        this.totalMovimientos = totalMovimientos;
    }
    
    public Boolean getEsGanador() {
        return esGanador;
    }
    
    public void setEsGanador(Boolean esGanador) {
        this.esGanador = esGanador;
    }
    
    // Métodos de utilidad
    public boolean estaActiva() {
        return Participacion.Estado.ACTIVO.equals(this.estado);
    }
    
    public boolean estaDestruida() {
        return Participacion.Estado.DESTRUIDO.equals(this.estado);
    }
    
    public boolean llegoAMeta() {
        return Participacion.Estado.EN_META.equals(this.estado);
    }
    
    public boolean ultimoMovimientoFueExitoso() {
        return Movimiento.Resultado.OK.equals(this.ultimoResultado);
    }
    
    public boolean ultimoMovimientoHuboColision() {
        return Movimiento.Resultado.COLISION_PARED.equals(this.ultimoResultado);
    }
    
    public String getPosicionActual() {
        return posX != null && posY != null ? "(" + posX + "," + posY + ")" : "(0,0)";
    }
    
    public String getVelocidadActual() {
        return velX != null && velY != null ? "(" + velX + "," + velY + ")" : "(0,0)";
    }
    
    @Override
    public String toString() {
        return "EstadoActualDTO{" +
                "idParticipacion=" + idParticipacion +
                ", estado=" + estado +
                ", ordenTurno=" + ordenTurno +
                ", turnoActual=" + turnoActual +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", jugadorNombre='" + jugadorNombre + '\'' +
                ", barcoAlias='" + barcoAlias + '\'' +
                '}';
    }
}
