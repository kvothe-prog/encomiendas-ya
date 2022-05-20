import ar.edu.unahur.obj2.encomiendas.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class EnvioDesc: DescribeSpec({

    val regionContinental = Continental()
    val regionInsular = Insular()

    val persona1 = Persona("Roberto Rodriguez", 20123159, regionContinental, 1156234578, "Gutenberg 130" )
    val persona2 = Persona("Pedro Rodriguez", 17156789, regionInsular, 1145567894, "Gutenberg 500" )
    val persona3 = Persona("Maria Rodriguez", 25114887, regionInsular, 1122345897, "Gutenberg 130" )
    val persona4 = Persona("Juana Rodriguez", 39548789, regionContinental, 112347895, "Gutenberg 130" )

    val avionWM = Avion(100000, 5000.00 , 10000.00 )
    val camionWM = Camion(100000, 100.00, 3000.00 )
    val motoR = Moto(10000, 1.0 , 50.00 )
    val camionR1 = Camion(100000, 100.00, 3000.00 )
    val camionR2 = Camion(100000, 100.00, 3000.00 )
    val barcoR = Buque(100000, 50000.00, 100000.00 )
    val camionBM = Camion(100000, 100.00, 3000.00 )
    val barcoBM = Buque(100000, 50000.00, 100000.00 )
    val avionBM1 = Avion(100000, 5000.00 , 10000.00 )
    val avionBM2 = Avion(100000, 5000.00 , 10000.00 )

    val sucursalWilliamMorris = Sucursal(34.582008 , 58.659693 , regionContinental, 100.00 , 100.00 , 100.00 )
    val sucursalRosario = Sucursal(32.963561 , 60.696283 , regionContinental, 100.00 , 100.00 , 100.00 )
    val sucursalBaseMarambio = Sucursal(64.242470 , 56.629179 , regionInsular, 100.00 , 100.00 , 100.00 )

    sucursalWilliamMorris.vehiculo.add(avionWM)
    sucursalWilliamMorris.vehiculo.add(camionWM)

    sucursalBaseMarambio.vehiculo.add(avionBM1)
    sucursalBaseMarambio.vehiculo.add(avionBM2)
    sucursalBaseMarambio.vehiculo.add(camionBM)
    sucursalBaseMarambio.vehiculo.add(barcoBM)

    describe("Casos propuestos") {
        it("caso 1") {
            val elementoNoPeligroso1 = Elemento(false , 20.00, 1.0,1.0, 1.0 )
            val elementoNoPeligroso2 = Elemento(false , 20.00, 1.0,1.0, 1.0 )
            val elementoNoPeligroso3 = Elemento(false , 20.00, 1.0,1.0, 1.0 )

            val envio1 = Envio(persona1, persona2, sucursalWilliamMorris, sucursalRosario, true, 100)
            val envio2 = Envio(persona1, persona3, sucursalWilliamMorris, sucursalBaseMarambio, true, 100)
            val envio3 = Envio(persona1, persona3, sucursalWilliamMorris, sucursalRosario, true, 100)

            envio1.paquetesCargados.add(elementoNoPeligroso1)
            envio2.paquetesCargados.add(elementoNoPeligroso2)
            envio3.paquetesCargados.add(elementoNoPeligroso3)

            avionWM.enviosCargados.add(envio2)
            camionWM.enviosCargados.add(envio1)

            sucursalWilliamMorris.vehiculosPuedenLlevar(envio3).size.shouldBe(1)
            sucursalWilliamMorris.vehiculosPuedenLlevar(envio3).first().shouldBe(camionWM)
        }

        it("caso 2"){
            val envio4 = Envio(persona3, persona4, sucursalWilliamMorris, sucursalRosario, true, 100)

            repeat(12){
                envio4.paquetesCargados.add(Elemento(true , 3.00, 1.0,1.0, 1.0 ))
            }

            sucursalWilliamMorris.deposito.add(envio4)

            sucursalWilliamMorris.vehiculosPuedenLlevar(envio4).size.shouldBe(1)
            sucursalWilliamMorris.vehiculosPuedenLlevar(envio4).first().shouldBe(camionWM)
        }

        it("caso 3"){
            val envio5 = Envio(persona1, persona2, sucursalBaseMarambio, sucursalRosario, true, 100)
            val envio6 = Envio(persona1, persona3, sucursalBaseMarambio, sucursalWilliamMorris, true, 100)

            val elementoNoPeligroso4 = Elemento(false , 100.00, 10.0,10.0, 15.0 )
            val elementoNoPeligroso5 = Elemento(false , 120.00, 10.0,10.0, 17.0 )
            val elementoNoPeligroso6 = Elemento(false , 130.00, 10.0,10.0, 20.0 )

            envio5.paquetesCargados.add(elementoNoPeligroso4)
            envio5.paquetesCargados.add(elementoNoPeligroso5)

            envio6.paquetesCargados.add(elementoNoPeligroso6)

            sucursalBaseMarambio.deposito.add(envio5)
            sucursalBaseMarambio.deposito.add(envio6)

            avionBM1.enviosCargados.add(envio5)

            sucursalBaseMarambio.vehiculosPuedenLlevar(envio6).size.shouldBe(2)
            sucursalBaseMarambio.vehiculosPuedenLlevar(envio6).shouldContain(avionBM2)
            sucursalBaseMarambio.vehiculosPuedenLlevar(envio6).shouldContain(barcoBM)
        }
    }

}
)