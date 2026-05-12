package com.tuituicoin.blockchain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testMineProducesProofOfWork() throws Exception {
        Transaction tx = new Transaction(1, "prev-hash", null, null);
        Block block = new Block(0, "prev-hash", tx);

        block.mine(2);
        System.out.println("[BlockTest] mined hash=" + block.getHash() + " nonce=" + block.getNonce());

        assertTrue(block.getHash().startsWith("00"));
        assertFalse(block.getHash().isEmpty());
        assertTrue(block.getNonce() >= 0);
    }

    @Test
    public void testHashChangesWhenNonceChanges() throws Exception {
        Transaction tx = new Transaction(1, "prev-hash", null, null);
        Block blockA = new Block(0, "prev-hash", tx);
        String firstHash = blockA.getHash();

        blockA.mine(1);
        String secondHash = blockA.getHash();
        System.out.println("[BlockTest] firstHash=" + firstHash + " secondHash=" + secondHash + " nonce=" + blockA.getNonce());

        assertFalse(firstHash.equals(secondHash));
        assertTrue(secondHash.startsWith("0"));
    }
}
