package br.com.dantebasso.pricemonitoring.models

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
import java.util.UUID

@MappedSuperclass
abstract class BaseModel: Identifiable {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    override var id: UUID? = null

}