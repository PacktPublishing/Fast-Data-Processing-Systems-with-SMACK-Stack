package packt.ch05

import java.util

import kafka.utils.VerifiableProperties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster

object SimplePartitioner {

  private var producer: KafkaProducer[String, String] = _
}

class SimplePartitioner(props: VerifiableProperties) extends Partitioner {

  def partition(key: AnyRef, a_numPartitions: Int): Int = {
    var partition = 0
    val partitionKey = key.asInstanceOf[String]
    val offset = partitionKey.lastIndexOf('.')
    if (offset > 0) {
      partition = java.lang.Integer.parseInt(partitionKey.substring(offset + 1)) %
        a_numPartitions
    }
    partition
  }

  override def partition(topic: String,
                         key: AnyRef,
                         keyBytes: Array[Byte],
                         value: AnyRef,
                         valueBytes: Array[Byte],
                         cluster: Cluster): Int = partition(key, 10)

  override def close() {
  }

  override def configure(configs: util.Map[String, _]) {
  }
}
