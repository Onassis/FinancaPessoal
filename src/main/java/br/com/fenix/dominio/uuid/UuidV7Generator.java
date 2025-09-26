package br.com.fenix.dominio.uuid;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

import org.hibernate.generator.EventType;
import org.hibernate.generator.Generator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UuidV7Generator extends SequenceStyleGenerator {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return UuidUtils.randomV7();
    }
}

//public class GenerateUuidV7 implements IdentifierGenerator, Generator {
//
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public Serializable generate(SharedSessionContractImplementor session, Object object) {
//		long timestamp = Instant.now().toEpochMilli();
//		long mostSigBits = (timestamp << 16) | 0x7000; // versão 7
//		long leastSigBits = UUID.randomUUID().getLeastSignificantBits();
//		return new UUID(mostSigBits, leastSigBits);
//	}
//	@Override
//	public EnumSet<EventType> getEventTypes() {
//		return EnumSet.of(EventType.INSERT);
//	}
//
//	@Override
//	public boolean generatedOnExecution() {
//		return true;
//	}
//}
//
////
////    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
//
//    public static UUID generateUuidV7() {
//        // Get current Unix timestamp in milliseconds
//    	System.out.println(Instant.now());
//        long timestampMillis = Instant.now().toEpochMilli();
//    	System.out.println(timestampMillis);
//        // 48-bit Unix timestamp
//        long msb = (timestampMillis & 0xFFFFFFFFFFFFL) << 16; // Shift to make room for version and variant
//        msb |= (0b0111L << 12); // Set version 7 (0111)
//        msb |= (0b10L << 10); // Set variant 10 (RFC 4122)
//
//        // Generate random data for the remaining bits
//        byte[] randomBytes = new byte[8];
//        SECURE_RANDOM.nextBytes(randomBytes);
//        long lsb = ByteBuffer.wrap(randomBytes).getLong();
//
//        // Combine the components
//        return new UUID(msb, lsb);
//    }
//
//    public static void main(String[] args) {
//    	  for (int i = 1; i <= 5; i++) {
//                 UUID uuidV7 = generateUuidV7();
//              System.out.println("Generated UUIDv7: " + uuidV7);
//    	  }
//    }
