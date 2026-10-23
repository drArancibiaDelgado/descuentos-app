package bo.edu.usfx.descuentos;

import org.springframework.stereotype.Service;

@Service
public class CalculadoraDescuentos {
    private static final double PRECIO_MAXIMO = 1_000_000_000.0;

    public double calcularPrecioFinal(double precio, double porcentaje) {
        if (Double.isInfinite(precio) || Double.isNaN(precio)) {
            throw new IllegalArgumentException("El precio debe ser un número finito");
        }
        if (Double.isInfinite(porcentaje) || Double.isNaN(porcentaje)) {
            throw new IllegalArgumentException("El porcentaje debe ser un número finito");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio original debe ser mayor que cero");
        }
        if (precio > PRECIO_MAXIMO) {
            throw new IllegalArgumentException("El precio debe ser a lo sumo " + PRECIO_MAXIMO);
        }
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }

        double descuento = precio * (porcentaje / 100.0);
        double total = precio - descuento;
        
        // Redondeo a 2 decimales para pasar las pruebas
        return Math.round(total * 100.0) / 100.0;
    }
}