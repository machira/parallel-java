package edu.coursera.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 */
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     * <p>
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {
        /*
         * TODO Declare a lock for this class to be used in implementing the
         * concurrent add, remove, and contains methods below.
         */

        private ReentrantLock lock;

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
            lock = new ReentrantLock();
        }

        /**
         * {@inheritDoc}
         *
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            lock.lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }
            boolean result = false;
            if (!object.equals(curr.object)) {
                final Entry entry = new Entry(object);
                entry.next = curr;
                pred.next = entry;
                result = true;
            }
            lock.unlock();
            return result;
        }

        /**
         * {@inheritDoc}
         *
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            lock.lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            boolean result = false;
            if (object.equals(curr.object)) {
                pred.next = curr.next;
                result = true;
            }
            lock.unlock();
            return result;
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            lock.lock();
            Entry curr = this.head;

            while (curr.object.compareTo(object) < 0) {
                curr = curr.next;
            }
            lock.unlock();
            return object.equals(curr.object);
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     * <p>
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {
        /*
         * TODO Declare a read-write lock for this class to be used in
         * implementing the concurrent add, remove, and contains methods below.
         */

        private ReadWriteLock rwLock;

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
            rwLock = new ReentrantReadWriteLock();
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            rwLock.writeLock().lock();
            boolean result = false;
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (!object.equals(curr.object)) {
                final Entry entry = new Entry(object);
                entry.next = curr;
                pred.next = entry;
                result = true;
            }
            rwLock.writeLock().unlock();
            return result;
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            rwLock.writeLock().lock();
            boolean result = false;
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (!object.equals(curr.object)) {
                pred.next = curr.next;
                result = true;
            }
            rwLock.writeLock().unlock();
            return result;
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            rwLock.readLock().lock();
            Entry curr = this.head;

            while (curr.object.compareTo(object) < 0) {
                curr = curr.next;
            }
            boolean res = object.equals(curr.object);
            rwLock.readLock().unlock();
            return res;
        }
    }
}
