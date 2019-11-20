package urjc.isi.pruebasSparkJava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


import static org.mockito.Mockito.*;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class MockitoTests {

	// El lenguaje Mock sirve para hacer test de forma que cuando estemos testeando algún método, y ese método
	// internamente llama a otros métodos nosotros no queremos que se llamen (tarda mucho y no es lo que queremos
	// testar) por lo tanto damos el cambiazo a esas llamadas. Mockito es uno de estos.
	
	// Travis comprobará estos tests antes de desplegar el proyecto en Heroku. Si pasa todos los tests despliega
	// en herocku la aplicación, sino no la lanza. Si se modifica el proyeco Travis vuelve a pasar los tests y
	// y despliega, continuamente.
	
	@Test
	public void MockListSize() {
		List list = mock(List.class); // Le pego el cambiazo a la clase list, para no llamar a sus métodos en test
		when(list.size()).thenReturn(10); // Cuando se llame al método size le decimos que devuelva el valor 10, sin llamar al método
		assertEquals(10, list.size()); // Este test pasará, ya que al llamar al método se devolverá un 10 automáticamente
	}

	@Test
	public void MockListSizeWithMultipleReturnValues() {
		List list = mock(List.class);
		when(list.size()).thenReturn(10).thenReturn(20); // La primera vez que le llame dvuelve 10. En las siguientes siempre un 20 (defino el comportamiento del método)
		assertEquals(10, list.size()); // Primera llamada
		assertEquals(20, list.size()); // Segunda llamada
		assertEquals(20, list.size()); // Tercera llamada y 
		      						  // subsiguientes devuelven el mismo valor
		
		verify(list, times(3)).size(); // Verifica que al objeto list se le a llamado 3 veces al método size
	}

	@Test
	public void MockListGet() {
		List<String> list = mock(List.class);
		when(list.get(0)).thenReturn("Hello World"); // Cuando llamé al método get con el parámetro 0 le digo que no llame al método, sino que devuelva "Hello World"
		assertEquals("Hello World", list.get(0));
		assertNull(list.get(1));
	}

	@Test
	public void MockListGetWithAny() {
		List<String> list = mock(List.class);
		when(list.get(anyInt())).thenReturn("Hello World");
		when(list.get(3)).thenReturn("Bye World");
		
		assertEquals("Hello World", list.get(0));
		assertEquals("Hello World", list.get(1));
		assertEquals("Hello World", list.get(2));
		assertEquals("Bye World", list.get(3));
		
		
	}
	
	@Test
	public void MockIterator_will_return_hello_world(){
		Iterator i = mock(Iterator.class);
		when(i.next()).thenReturn("Hello").thenReturn("World");

		String result=i.next()+" "+i.next();

		assertEquals("Hello World", result);
	}


	@Test
	public void MockWithArguments(){
		Comparable c=mock(Comparable.class);
		when(c.compareTo("Test")).thenReturn(1);
		assertEquals(1,c.compareTo("Test"));
		assertEquals(0,c.compareTo("Foo"));
	}
	

	@Test
	public void MockWithUnspecifiedArguments(){
		Comparable c=mock(Comparable.class);
		when(c.compareTo(anyInt())).thenReturn(-1);
		when(c.compareTo(3)).thenReturn(0);
		assertEquals(-1, c.compareTo(5));
		assertEquals(0, c.compareTo(3));
		verify(c).compareTo(5);
		verify(c).compareTo(3);
		verify(c, never()).compareTo(25);
		verify(c, times(1)).compareTo(5);
		verify(c, atLeastOnce()).compareTo(5);
		verify(c, atLeast(1)).compareTo(5);
	}


	@Test(expected=IOException.class)
	public void MockOutputStreamWriterRethrowsAnExceptionFromOutputStream() 
			throws IOException{
		OutputStream mock=mock(OutputStream.class);
		OutputStreamWriter osw=new OutputStreamWriter(mock);
		doThrow(new IOException()).when(mock).close();
		osw.close();
	}
	

	@Test
	public void MockOutputStreamWriterClosesOutputStreamOnClose()
			throws IOException{
		OutputStream mock=mock(OutputStream.class);
		OutputStreamWriter osw=new OutputStreamWriter(mock);
		osw.close();
		verify(mock).close();
	}

}
