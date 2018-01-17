package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.concurrent.atomic.AtomicInteger;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determine the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    AtomicInteger atomicIntegerInteger;

    @Override
    public int countPrimes(final int limit) {

        if (limit < 2) {
            return 0;
        }
         atomicIntegerInteger = new AtomicInteger(1);
        finish(() -> {
            SieveActorActor actor = new SieveActorActor(2);

            for (int i = 3; i <= limit; i += 2) {
                actor.send(i);
            }
        });

        return atomicIntegerInteger.get();
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public final class SieveActorActor extends Actor {

        private int localPrime;
        SieveActorActor nextActor;

        SieveActorActor(int localPrime) {
            this.localPrime = localPrime;
        }

        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */

        @Override
        public void process(final Object msg) {
            int candidate = (int) msg;
            if (candidate <= 0) {
                return;
            }

            if (locallyPrime(candidate)) {
                if (nextActor == null) {
                    atomicIntegerInteger.incrementAndGet();
                    nextActor = new SieveActorActor(candidate);
                }
                    nextActor.send(candidate);
            }
        }

        private boolean locallyPrime(int candidate) {
            return candidate % localPrime != 0;
        }
    }
}
