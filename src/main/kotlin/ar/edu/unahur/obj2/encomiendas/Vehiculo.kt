package ar.edu.unahur.obj2.encomiendas
import Envio
import java.lang.Double.max
import java.lang.Double.min

abstract class Vehiculo(
    open val kilometros : Int,
    open var volumen : Double,
    open val pesoMaximo: Double,
    open var tipo: TipoVehiculo) {

    var enviosCargados : MutableList<Envio> = mutableListOf()
    abstract val velocidadPromedio: Int
    abstract var llevaPeligroso: Boolean
    open val esLargaDistancia : Boolean = true

    abstract fun volumenSoportado() : Double
    abstract fun pesoMaximo() : Double
    abstract fun costo(unEnvio: Envio) : Double

    abstract fun seDesplazaEn() : MutableList<Region>
    abstract fun distanciaDe(unEnvio: Envio) : Double

    fun tiempoEnViaje(unEnvio: Envio) : Double = distanciaDe(unEnvio)/velocidadPromedio
    fun pesoCargado(): Double {
        return enviosCargados.sumOf{it.pesoDeclarado()}
    }
    fun volumenEnVehiculo(): Double{
        return enviosCargados.sumOf{it.volumenDeclarado()}
    }

    open fun tieneDistanciaPermitida(unEnvio: Envio) : Boolean = esLargaDistancia
    fun tienePesoPermitido(unEnvio: Envio): Boolean = pesoMaximo() >= (unEnvio.pesoDeclarado() + pesoCargado())
    fun tieneVolumenPermitido(unEnvio: Envio): Boolean = volumenSoportado() >= (unEnvio.volumenDeclarado() + volumenEnVehiculo())
    fun tieneMismoDestinoQue(unEnvio: Envio): Boolean{
        var resultado = true
        if(enviosCargados.isNotEmpty()){
            resultado = enviosCargados.all{it.destino == unEnvio.destino}
        }
        return resultado
    }
    fun estaEnRegion(unEnvio: Envio): Boolean{
        var resultado: Boolean
        if(unEnvio.origen.region.tipo == unEnvio.destino.region.tipo){
            resultado = seDesplazaEn().any{it.tipo == unEnvio.origen.region.tipo}
        }else{
            resultado = seDesplazaEn().any{it.tipo == unEnvio.origen.region.tipo} && seDesplazaEn().any{it.tipo == unEnvio.destino.region.tipo}
        }
        return resultado
    }
    fun puedeLlevar(unEnvio: Envio): Boolean{
       return  tienePesoPermitido(unEnvio) && tieneVolumenPermitido(unEnvio) && tieneMismoDestinoQue(unEnvio) && tieneDistanciaPermitida(unEnvio) && estaEnRegion(unEnvio)
    }
}
enum class TipoVehiculo{MOTO, CAMION, AVION, BUQUE}
class Moto(
    kilometros: Int,
    volumen: Double,
    pesoMaximo: Double,
    tipo: TipoVehiculo = TipoVehiculo.MOTO): Vehiculo(kilometros, volumen, pesoMaximo, tipo){

    override val velocidadPromedio: Int = 110
    override var llevaPeligroso: Boolean = false
    override val esLargaDistancia : Boolean = false

    override fun volumenSoportado(): Double = 1.0
    override fun pesoMaximo(): Double = pesoMaximo
    override fun costo(unEnvio: Envio) : Double = max(80.0 * distanciaDe(unEnvio),500.0)

    override fun distanciaDe(unEnvio: Envio) : Double = unEnvio.origen.distanciaTierra(unEnvio.destino)
    override fun seDesplazaEn() : MutableList<Region> = mutableListOf(Continental())
    override fun tieneDistanciaPermitida(unEnvio: Envio) : Boolean = distanciaDe(unEnvio) <= 400
}
class Camion(
    kilometros: Int,
    volumen: Double,
    pesoMaximo: Double,
    tipo: TipoVehiculo = TipoVehiculo.CAMION): Vehiculo(kilometros, volumen, pesoMaximo, tipo) {

    override val velocidadPromedio: Int = 90
    override var llevaPeligroso: Boolean = true

    override fun volumenSoportado() : Double = volumen
    override fun pesoMaximo(): Double = max(3000.0 - desgastePorKilometro(), 500.0)
    override fun costo(unEnvio: Envio) : Double = 300.0 * distanciaDe(unEnvio)

    override fun distanciaDe(unEnvio: Envio) : Double = unEnvio.origen.distanciaTierra(unEnvio.destino)
    private fun desgastePorKilometro() : Double = kilometros / 100.0
    override fun seDesplazaEn() : MutableList<Region> = mutableListOf(Continental())
}
class Avion(
    kilometros: Int,
    volumen: Double,
    pesoMaximo: Double,
    tipo: TipoVehiculo = TipoVehiculo.AVION): Vehiculo(kilometros, volumen, pesoMaximo, tipo) {

    override val velocidadPromedio: Int = 700
    override var llevaPeligroso: Boolean = cantidadDePeligrosos() <= 10

    override fun volumenSoportado() : Double = min(volumen, 5000.0)
    override fun pesoMaximo(): Double = 10000.0
    override fun costo(unEnvio: Envio) : Double = if(unEnvio.pesoDeclarado() <= 1500.0) {2000.0} else { 2500.0 } * distanciaDe(unEnvio)

    private fun cantidadDePeligrosos() = enviosCargados.count{it.esPeligroso()}
    override fun distanciaDe(unEnvio: Envio) : Double = unEnvio.origen.distanciaDirecta(unEnvio.destino)
    override fun seDesplazaEn() : MutableList<Region> = mutableListOf(Insular(), Continental())

}
class Buque(
    kilometros: Int,
    volumen: Double = 50000.0,
    pesoMaximo: Double,
    tipo: TipoVehiculo = TipoVehiculo.BUQUE): Vehiculo(kilometros, volumen, pesoMaximo, tipo) {

    override val velocidadPromedio: Int = 60
    override var llevaPeligroso: Boolean = cantidadDePeligrosos() <= 100

    override fun volumenSoportado() : Double = min(volumen, 50000.0)
    override fun pesoMaximo(): Double = 100000.0

    private fun cantidadDePeligrosos() = enviosCargados.count{it.esPeligroso()}
    override fun distanciaDe(unEnvio: Envio) : Double = unEnvio.origen.distanciaDirecta(unEnvio.destino)
    override fun costo(unEnvio: Envio) : Double = if(unEnvio.pesoDeclarado() <= 1500.0) {200.0} else { 250.0 } * distanciaDe(unEnvio)
    override fun seDesplazaEn() : MutableList<Region> = mutableListOf(Continental(),Insular())
}