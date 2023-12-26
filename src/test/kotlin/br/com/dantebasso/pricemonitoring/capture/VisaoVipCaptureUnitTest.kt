package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.capture.adapters.DimensionProductAdapter
import br.com.dantebasso.pricemonitoring.processor.VisaoVipLineProcessor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class VisaoVipCaptureUnitTest {

    @Autowired
    private lateinit var dimensionProductAdapter: DimensionProductAdapter

    @Autowired
    private lateinit var visaoVipLineProcessor: VisaoVipLineProcessor


    @Test
    fun test() {
        val toTest = VisaoVipCapture(
            processor = visaoVipLineProcessor
        )
        toTest.capture()
    }

}