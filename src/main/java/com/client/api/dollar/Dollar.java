package com.client.api.dollar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Dollar {

    private Double compra;
    private Double venta;
    private String casa;
    private String nombre;
    private String moneda;
    private String fechaActualizacion;
}
