import { describe, it, expect } from 'vitest'
import { aplicarCupon, Cupon } from './cupones'

describe('aplicarCupon - Ronda 3 (Con reglas y mutantes cazados)', () => {
  const cuponBase: Cupon = { codigo: 'USFX10', porcentaje: 10, topeBs: 50, vence: '2026-12-31' }

  describe('Regla 1: El precio debe ser mayor que cero', () => {
    const hoy = new Date('2026-10-16T12:00:00Z')

    it('rechaza precio negativo', () => {
      expect(aplicarCupon(-1, cuponBase, hoy)).toEqual({
        ok: false,
        motivo: 'El precio original debe ser mayor que cero',
      })
    })

    it('rechaza precio cero (límite en)', () => {
      expect(aplicarCupon(0, cuponBase, hoy)).toEqual({
        ok: false,
        motivo: 'El precio original debe ser mayor que cero',
      })
    })

    it('acepta precio mayor a cero (límite después)', () => {
      expect(aplicarCupon(1, cuponBase, hoy)).toEqual({
        ok: true,
        descuentoBs: 0.1,
        precioFinal: 0.9,
      })
    })
  })

  describe('Regla 2: El código de un cupón válido tiene 4 letras mayúsculas y 2 dígitos', () => {
    const hoy = new Date('2026-10-16T12:00:00Z')

    it('acepta código en minúsculas y con espacios a los lados', () => {
      const cuponSucia: Cupon = { ...cuponBase, codigo: '  usfx10 ' }
      expect(aplicarCupon(100, cuponSucia, hoy)).toEqual({
        ok: true,
        descuentoBs: 10,
        precioFinal: 90,
      })
    })

    it('rechaza código sin números', () => {
      const cuponMalo: Cupon = { ...cuponBase, codigo: 'USFXXX' }
      expect(aplicarCupon(100, cuponMalo, hoy)).toEqual({
        ok: false,
        motivo: 'El código del cupón no es válido',
      })
    })

    it('rechaza código con tamaño incorrecto', () => {
      const cuponMalo: Cupon = { ...cuponBase, codigo: 'USFX100' }
      expect(aplicarCupon(100, cuponMalo, hoy)).toEqual({
        ok: false,
        motivo: 'El código del cupón no es válido',
      })
    })

    // TEST NUEVO PARA CAZAR EL MUTANTE DE REGEX (Sobreviviente 1)
    it('rechaza código que contiene el patrón pero tiene caracteres extra al inicio', () => {
      const cuponMalo: Cupon = { ...cuponBase, codigo: 'XUSFX10' }
      expect(aplicarCupon(100, cuponMalo, hoy)).toEqual({
        ok: false,
        motivo: 'El código del cupón no es válido',
      })
    })
  })

  describe('Regla 3: Un cupón vale hasta el día de su vencimiento inclusive', () => {
    const cupon: Cupon = { ...cuponBase, vence: '2026-10-15' }

    it('acepta cupón el día anterior al vencimiento (límite antes)', () => {
      const hoy = new Date('2026-10-14T23:59:59Z')
      expect(aplicarCupon(100, cupon, hoy)).toEqual({ ok: true, descuentoBs: 10, precioFinal: 90 })
    })

    it('acepta cupón el mismo día del vencimiento (límite en)', () => {
      const hoy = new Date('2026-10-15T12:00:00Z')
      expect(aplicarCupon(100, cupon, hoy)).toEqual({ ok: true, descuentoBs: 10, precioFinal: 90 })
    })

    it('rechaza cupón un día después del vencimiento (límite después)', () => {
      const hoy = new Date('2026-10-16T00:00:01Z')
      expect(aplicarCupon(100, cupon, hoy)).toEqual({ ok: false, motivo: 'El cupón venció' })
    })
  })

  describe('Regla 4 y 5: Descuento, redondeo, tope y precio final', () => {
    const hoy = new Date('2026-10-16T12:00:00Z')

    it('aplica descuento normal y redondea correctamente', () => {
      expect(aplicarCupon(33.33, cuponBase, hoy)).toEqual({
        ok: true,
        descuentoBs: 3.33,
        precioFinal: 30,
      })
    })

    it('respeta el tope del cupón (límite superado)', () => {
      expect(aplicarCupon(1000, cuponBase, hoy)).toEqual({
        ok: true,
        descuentoBs: 50,
        precioFinal: 950,
      })
    })

    it('no supera el tope del cupón (límite exacto)', () => {
      expect(aplicarCupon(500, cuponBase, hoy)).toEqual({
        ok: true,
        descuentoBs: 50,
        precioFinal: 450,
      })
    })

    it('el precio final no es negativo si el descuento supera el precio', () => {
      const superCupon: Cupon = { ...cuponBase, porcentaje: 200, topeBs: 100 }
      expect(aplicarCupon(40, superCupon, hoy)).toEqual({
        ok: true,
        descuentoBs: 80,
        precioFinal: 0,
      })
    })

    // TEST NUEVO PARA CAZAR EL MUTANTE DE EPSILON (Sobreviviente 2)
    it('redondea correctamente hacia arriba en el límite matemático exacto (X.XX5)', () => {
      // Precio 10.05 * 10% = 1.005. Debe redondear a 1.01 gracias al Number.EPSILON
      expect(aplicarCupon(10.05, cuponBase, hoy)).toEqual({
        ok: true,
        descuentoBs: 1.01,
        precioFinal: 9.04,
      })
    })
  })

  describe('Otras validaciones', () => {
    const hoy = new Date('2026-10-16T12:00:00Z')

    it('rechaza la operación cuando no se envía cupón', () => {
      expect(aplicarCupon(100, null, hoy)).toEqual({ ok: false, motivo: 'No hay cupón' })
    })
  })
})
