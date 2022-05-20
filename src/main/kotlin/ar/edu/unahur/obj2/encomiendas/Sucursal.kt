package ar.edu.unahur.obj2.encomiendas

import Envio
import java.lang.Math.abs
import java.lang.Math.sqrt


class Sucursal(val posicionX: Double,val posicionY: Double,val region: Region,val ancho: Double,val alto: Double, profundidad:Double) {
    val volumenSucursal: Double = ancho * alto * profundidad
    var vehiculo: MutableList<Vehiculo> = mutableListOf()
    var deposito: MutableList<Envio> = mutableListOf()
    fun distanciaTierra(puntoB: Sucursal): Double {
        return abs(this.posicionX - puntoB.posicionX) + abs(this.posicionY - puntoB.posicionY)
    }
    fun distanciaDirecta(puntoB: Sucursal): Double{
        return sqrt(Math.pow(this.posicionX - puntoB.posicionX,2.0) + Math.pow(this.posicionY - puntoB.posicionY,2.0))
    }
    fun volumenEnDeposito(): Double{
        return deposito.sumOf{it.volumenDeclarado()}
    }
    fun travelPuedeLlevar(unEnvio: Envio): Boolean{
        return vehiculo.any{it.puedeLlevar(unEnvio)} && hayLugar(unEnvio,unEnvio.destino)
    }
    fun hayLugar(unEnvio: Envio, unaSucursal: Sucursal): Boolean {
        return volumenSucursal >= (unEnvio.volumenDeclarado() + unaSucursal.volumenEnDeposito())
    }
    fun vehiculosPuedenLlevar(unEnvio: Envio): List<Vehiculo> {
        return vehiculo.filter{ it.puedeLlevar(unEnvio) }
    }
    fun vehiculoMasBarato(unEnvio: Envio): Vehiculo? {
        return vehiculosPuedenLlevar(unEnvio).minByOrNull{ it.costo(unEnvio) }
    }
    fun vehiculoParaPrioritario(unEnvio: Envio): Vehiculo? {
        return vehiculosPuedenLlevar(unEnvio).filter{ it.tiempoEnViaje(unEnvio) <= unEnvio.tiempoDeEnvio }.minByOrNull{ it.costo(unEnvio) }
    }
    fun costoEnvio(unEnvio: Envio): Double {
        var costo : Double = 0.0
        if (unEnvio.envioEstandar && travelPuedeLlevar(unEnvio) ){
            costo = vehiculoMasBarato(unEnvio)?.costo(unEnvio)!!
        }else if(!unEnvio.envioEstandar && travelPuedeLlevar(unEnvio)){
            costo = vehiculoParaPrioritario(unEnvio)?.costo(unEnvio)!! * 1.5
        }
        return costo
    }
    fun cargarEnVehiculo(unEnvio: Envio){
        if(unEnvio.envioEstandar && travelPuedeLlevar(unEnvio)) {
            vehiculoMasBarato(unEnvio)?.enviosCargados?.add(unEnvio)
            unEnvio.destino.deposito.add(unEnvio)
        }else if(!unEnvio.envioEstandar && travelPuedeLlevar(unEnvio)) {
            vehiculoParaPrioritario(unEnvio)?.enviosCargados?.add(unEnvio)
            unEnvio.destino.deposito.add(unEnvio)
        }else if(hayLugar(unEnvio,this)){
            deposito.add(unEnvio)
        }else{
            error("no podemos cargar tu envio")
        }
    }
}

enum class RegionTipo{INSULAR, CONTINENTAL}

open class Region(var tipo: RegionTipo) {
    open lateinit var listaProvincia: MutableList<String>
}
class Insular(tipo: RegionTipo = RegionTipo.INSULAR): Region(tipo) {
    override var listaProvincia : MutableList<String> = mutableListOf("Malvinas", "Ushuaia", "la Base Marambio", "Antártida" )
}
class Continental(tipo: RegionTipo = RegionTipo.CONTINENTAL): Region(tipo) {
    override var listaProvincia : MutableList<String> = mutableListOf("Córdoba", "Jujuy", "Buenos Aires", "Rosario")
}