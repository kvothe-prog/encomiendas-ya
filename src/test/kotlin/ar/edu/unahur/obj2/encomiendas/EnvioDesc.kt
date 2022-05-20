import ar.edu.unahur.obj2.encomiendas.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class EnvioDesc: DescribeSpec({

    val regionContinental = Continental()
    val regionInsular = Insular()

    val persona1 = Persona("Roberto Rodriguez", 20123159, regionContinental, 1156234578, "Gutenberg 130" )
    val persona2 = Persona("Pedro Rodriguez", 17156789, regionInsular, 1145567894, "Gutenberg 500" )
    val persona3 = Persona("Maria Rodriguez", 25114887, regionInsular, 1122345897, "Gutenberg 130" )
    val persona4 = Persona("Juana Rodriguez", 39548789, regionContinental, 112347895, "Gutenberg 130" )

    val avionWM = Avion(100000, 5000.00 , 10000.00 )
    val camionWM = Camion(100000, 50.00, 3000.00 )
    val motoR = Moto(10000, 1.0 , 50.00 )
    val camionR1 = Camion(100000, 50.00, 3000.00 )
    val camionR2 = Camion(100000, 50.00, 3000.00 )
    val barcoR = Buque(100000, 50000.00, 100000.00 )
    val camionBM = Camion(100000, 50.00, 3000.00 )
    val barcoBM = Buque(100000, 50000.00, 100000.00 )
    val avionBM1 = Avion(100000, 5000.00 , 10000.00 )
    val avionBM2 = Avion(100000, 5000.00 , 10000.00 )

    val sucursalWilliamMorris = Sucursal(34.582008 , 58.659693 , regionContinental, 100.00 , 100.00 , 100.00 )
    val sucursalRosario = Sucursal(32.963561 , 60.696283 , regionContinental, 100.00 , 100.00 , 100.00 )
    val sucursalBaseMarambio = Sucursal(64.242470 , 56.629179 , regionInsular, 100.00 , 100.00 , 100.00 )

    val envio1 = Envio(persona1, persona2, sucursalWilliamMorris, sucursalRosario, true, 100)
    val envio2 = Envio(persona1, persona3, sucursalWilliamMorris, sucursalBaseMarambio, true, 100)
    val envio3 = Envio(persona1, persona3, sucursalWilliamMorris, sucursalRosario, true, 100)

    sucursalWilliamMorris.vehiculo.add(avionWM)
    sucursalWilliamMorris.vehiculo.add(camionWM)

    avionWM.enviosCargados.add(envio2)
    camionWM.enviosCargados.add(envio1)


    describe("Casos propuestos") {
        it("caso 1") {
            sucursalWilliamMorris.vehiculosPuedenLlevar(envio3).shouldBe(camionWM)
        }
    }

}
)