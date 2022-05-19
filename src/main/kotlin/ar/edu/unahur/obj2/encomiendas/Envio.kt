import ar.edu.unahur.obj2.encomiendas.Persona
import ar.edu.unahur.obj2.encomiendas.Sucursal

class Envio(val remitente: Persona, val destinatario : Persona, val origen: Sucursal, var destino: Sucursal, var envioEstandar : Boolean = true, var tiempoDeEnvio: Int) {
    companion object{
        var correccion : Double = 1.05 //+5%
    }

    var paquetesCargados : MutableList<Elemento> = mutableListOf()
    fun pesoDeclarado(): Double {
        return paquetesCargados.sumOf{ it.peso}
    }
    fun volumenDeclarado():Double{
        return paquetesCargados.sumOf{ it.volumenElemento} * correccion
    }
    fun esPeligroso() : Boolean = paquetesCargados.any{it.peligroso}
}

class Elemento(var peligroso: Boolean,
               var peso: Double,
               val ancho: Double,
               val alto: Double,
               val profundidad: Double){
    val volumenElemento: Double = profundidad * ancho * alto
}