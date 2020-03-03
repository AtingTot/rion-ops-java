package com.nanosai.rionops.rion.read.object;

import com.nanosai.rionops.rion.RionFieldTypes;
import com.nanosai.rionops.rion.pojo.PojoCyclic;
import com.nanosai.rionops.rion.pojo.PojoGraphNode;
import com.nanosai.rionops.rion.write.object.RionObjectWriter;
import com.nanosai.rionops.rion.write.object.RionObjectWriterBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RionObjectReaderCyclicTest {

    @Test
    public void testPojoCyclic() {
        byte[] dest = new byte[1024];

        RionObjectWriterBuilder writerBuilder = new RionObjectWriterBuilder();
        writerBuilder.addDeclaredFields(PojoCyclic.class);

        RionObjectWriter writer = writerBuilder.build();

        PojoCyclic pojoParent = new PojoCyclic();
        PojoCyclic pojoChild  = new PojoCyclic();

        pojoParent.child = pojoChild;
        pojoChild.parent = pojoParent;

        int bytesWritten = writer.writeCyclic(pojoParent, 2, dest, 0);

        RionObjectReaderBuilder readerBuilder = new RionObjectReaderBuilder();
        RionObjectReader reader = readerBuilder.setTypeClass(PojoCyclic.class).addDeclaredFieldsForTypeClass().build();

        PojoCyclic pojoCyclicOut = (PojoCyclic) reader.readCyclic(dest, 0);

        assertNotNull(pojoCyclicOut);
        assertNotSame(pojoCyclicOut, pojoParent);
        assertNotSame(pojoCyclicOut, pojoChild);

        assertNull(pojoCyclicOut.parent);
        assertNotNull((pojoCyclicOut.child));

        assertNull((pojoCyclicOut.child.child));
        assertNotNull((pojoCyclicOut.child.parent));

        assertNotSame(pojoCyclicOut, pojoCyclicOut.child);
        assertSame   (pojoCyclicOut, pojoCyclicOut.child.parent);
    }

    @Test
    public void testPojoGraphNode()  {
        byte[] dest = new byte[1024];

        RionObjectWriterBuilder writerBuilder = new RionObjectWriterBuilder();
        writerBuilder.addDeclaredFields(PojoGraphNode.class);

        RionObjectWriter writer = writerBuilder.build();

        RionObjectReaderBuilder readerBuilder = new RionObjectReaderBuilder();
        RionObjectReader reader = readerBuilder.setTypeClass(PojoGraphNode.class).addDeclaredFieldsForTypeClass().build();


        PojoGraphNode root  = new PojoGraphNode();
        root.up    = new PojoGraphNode();
        root.down  = new PojoGraphNode();
        root.left  = new PojoGraphNode();
        root.right = new PojoGraphNode();

        root.up.up       = root.down;
        root.right.right = root.left;

        int bytesWritten = writer.writeCyclic(root, 2, dest, 0);

        testSerializedPojoGraphNode(dest, bytesWritten);

        PojoGraphNode pojoOut = (PojoGraphNode) reader.readCyclic(dest, 0);
        assertNotNull(pojoOut);
        assertNotNull(pojoOut.up);
        assertNotNull(pojoOut.up.up);
        assertNotNull(pojoOut.down);
        assertNotNull(pojoOut.left);
        assertNotNull(pojoOut.right);
        assertNotNull(pojoOut.right.right);

        assertSame(pojoOut.up.up, pojoOut.down);
        assertSame(pojoOut.right.right, pojoOut.left);

        assertNotSame(pojoOut.up, pojoOut.down);
        assertNotSame(pojoOut.up, pojoOut.left);
        assertNotSame(pojoOut.up, pojoOut.right);
        assertNotSame(pojoOut.down, pojoOut.left);
        assertNotSame(pojoOut.down, pojoOut.right);
        assertNotSame(pojoOut.left, pojoOut.right);


    }

    private void testSerializedPojoGraphNode(byte[] dest, int bytesWritten) {
        assertEquals(130, bytesWritten);

        int index = 0;
        assertEquals(0xC2, 0xFF & dest[index++]);
        assertEquals(0, 0xFF & dest[index++]);
        assertEquals(127, 0xFF & dest[index++]);

        assertEquals(0xE2, 0xFF & dest[index++]);
        assertEquals( 'u', 0xFF & dest[index++]);
        assertEquals( 'p', 0xFF & dest[index++]);

        assertEquals(0xc2, 0xFF & dest[index++]);
        assertEquals(   0, 0xFF & dest[index++]);
        assertEquals(  48, 0xFF & dest[index++]);

            assertEquals(0xe2, 0xFF & dest[index++]);
            assertEquals( 'u', 0xFF & dest[index++]);
            assertEquals( 'p', 0xFF & dest[index++]);

            assertEquals(0xc2, 0xFF & dest[index++]);
            assertEquals(   0, 0xFF & dest[index++]);
            assertEquals(  23, 0xFF & dest[index++]);

                assertEquals(0xe2, 0xFF & dest[index++]);
                assertEquals( 'u', 0xFF & dest[index++]);
                assertEquals( 'p', 0xFF & dest[index++]);

                assertEquals(0xc0, 0xFF & dest[index++]);

                assertEquals(0xe4, 0xFF & dest[index++]);
                assertEquals( 'd', 0xFF & dest[index++]);
                assertEquals( 'o', 0xFF & dest[index++]);
                assertEquals( 'w', 0xFF & dest[index++]);
                assertEquals( 'n', 0xFF & dest[index++]);

                assertEquals(0xc0, 0xFF & dest[index++]);

                assertEquals(0xe4, 0xFF & dest[index++]);
                assertEquals( 'l', 0xFF & dest[index++]);
                assertEquals( 'e', 0xFF & dest[index++]);
                assertEquals( 'f', 0xFF & dest[index++]);
                assertEquals( 't', 0xFF & dest[index++]);

                assertEquals(0xc0, 0xFF & dest[index++]);

                assertEquals(0xe5, 0xFF & dest[index++]);
                assertEquals( 'r', 0xFF & dest[index++]);
                assertEquals( 'i', 0xFF & dest[index++]);
                assertEquals( 'g', 0xFF & dest[index++]);
                assertEquals( 'h', 0xFF & dest[index++]);
                assertEquals( 't', 0xFF & dest[index++]);

                assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'd', 0xFF & dest[index++]);
            assertEquals( 'o', 0xFF & dest[index++]);
            assertEquals( 'w', 0xFF & dest[index++]);
            assertEquals( 'n', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'l', 0xFF & dest[index++]);
            assertEquals( 'e', 0xFF & dest[index++]);
            assertEquals( 'f', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe5, 0xFF & dest[index++]);
            assertEquals( 'r', 0xFF & dest[index++]);
            assertEquals( 'i', 0xFF & dest[index++]);
            assertEquals( 'g', 0xFF & dest[index++]);
            assertEquals( 'h', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

        assertEquals(0xe4, 0xFF & dest[index++]);
        assertEquals( 'd', 0xFF & dest[index++]);
        assertEquals( 'o', 0xFF & dest[index++]);
        assertEquals( 'w', 0xFF & dest[index++]);
        assertEquals( 'n', 0xFF & dest[index++]);

        assertEquals(0xf1, 0xFF & dest[index++]);
        assertEquals(   0, 0xFF & dest[index++]);
        assertEquals(   2, 0xFF & dest[index++]);

        assertEquals(0xe4, 0xFF & dest[index++]);
        assertEquals( 'l', 0xFF & dest[index++]);
        assertEquals( 'e', 0xFF & dest[index++]);
        assertEquals( 'f', 0xFF & dest[index++]);
        assertEquals( 't', 0xFF & dest[index++]);

        assertEquals(0xc2, 0xFF & dest[index++]);
        assertEquals(   0, 0xFF & dest[index++]);
        assertEquals(  23, 0xFF & dest[index++]);

            assertEquals(0xe2, 0xFF & dest[index++]);
            assertEquals( 'u', 0xFF & dest[index++]);
            assertEquals( 'p', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'd', 0xFF & dest[index++]);
            assertEquals( 'o', 0xFF & dest[index++]);
            assertEquals( 'w', 0xFF & dest[index++]);
            assertEquals( 'n', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'l', 0xFF & dest[index++]);
            assertEquals( 'e', 0xFF & dest[index++]);
            assertEquals( 'f', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe5, 0xFF & dest[index++]);
            assertEquals( 'r', 0xFF & dest[index++]);
            assertEquals( 'i', 0xFF & dest[index++]);
            assertEquals( 'g', 0xFF & dest[index++]);
            assertEquals( 'h', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

        assertEquals(0xe5, 0xFF & dest[index++]);
        assertEquals( 'r', 0xFF & dest[index++]);
        assertEquals( 'i', 0xFF & dest[index++]);
        assertEquals( 'g', 0xFF & dest[index++]);
        assertEquals( 'h', 0xFF & dest[index++]);
        assertEquals( 't', 0xFF & dest[index++]);


        assertEquals(0xc2, 0xFF & dest[index++]);
        assertEquals(   0, 0xFF & dest[index++]);
        assertEquals(  25, 0xFF & dest[index++]);

            assertEquals(0xe2, 0xFF & dest[index++]);
            assertEquals( 'u', 0xFF & dest[index++]);
            assertEquals( 'p', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'd', 0xFF & dest[index++]);
            assertEquals( 'o', 0xFF & dest[index++]);
            assertEquals( 'w', 0xFF & dest[index++]);
            assertEquals( 'n', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe4, 0xFF & dest[index++]);
            assertEquals( 'l', 0xFF & dest[index++]);
            assertEquals( 'e', 0xFF & dest[index++]);
            assertEquals( 'f', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xc0, 0xFF & dest[index++]);

            assertEquals(0xe5, 0xFF & dest[index++]);
            assertEquals( 'r', 0xFF & dest[index++]);
            assertEquals( 'i', 0xFF & dest[index++]);
            assertEquals( 'g', 0xFF & dest[index++]);
            assertEquals( 'h', 0xFF & dest[index++]);
            assertEquals( 't', 0xFF & dest[index++]);

            assertEquals(0xf1, 0xFF & dest[index++]);
            assertEquals(   0, 0xFF & dest[index++]);
            assertEquals(   3, 0xFF & dest[index++]);

            assertEquals(130, index);

    }
}
