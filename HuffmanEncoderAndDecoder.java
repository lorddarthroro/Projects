//Rohaan Ahmad
//CSC 172
//Project 2
//27 October 2021

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HuffmanEncoderAndDecoder implements Huffman {
	public static void main(String[] args){
	      Huffman  huffman = new HuffmanEncoderAndDecoder();
	      //FOR READER - replace "roko.txt" with the name/directory to your file
	      huffman.encode("roko.txt", "roko.enc", "freq.txt");
	      huffman.decode("roko.enc", "roko_dec.txt", "freq.txt");
			// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
			// On linux and mac, you can use `diff' command to check if they are the same. 
	   }
	
	//node class needs to be comparable bc priority queue needs comparable as input (USED FOR OLD IMPLEMENTATION, DOENSNT NEED THIS ANYMORE
	public static class node implements Comparable<node>{
		public char value; //ascii value of the char
		public int key; //key will be determined by frequency
		public node rightchild;
		public node leftchild;
		//important for making a priority queue
		public node n; //next pointer
		public node p; //prev pointer
		
		public node next() { return n; } // Return next link
		public node setNext(node nextval) { return n = nextval; } // Set next link
		public node prev() { return p; } // Return prev link
		public node setPrev(node prevval) { return p = prevval; }//set prev link
		
		
		public node(char v, int k, node lc, node rc) {
			this.key = k;
			this.value = v;
			this.leftchild = lc;
			this.rightchild = rc;
			
		}
		
		@Override
		public int compareTo(node x) {
			//returns result of comparison between node and inputed node
			if(Integer.compare(this.key, x.key)!=0){
				return Integer.compare(this.key, x.key);
			}
			//if freq are the same, compare value instead
			else {
				return Integer.compare(this.value, x.value);
			}
		}
	}
  
	// Feel free to add more methods and variables as required. 
 
	public void encode(String inputFile, String outputFile, String freqFile){
		BinaryIn input = new BinaryIn(inputFile);
		BinaryIn inputDuplicate = new BinaryIn(inputFile); //duplicate created so I can create a key and encode , one for each
		BinaryOut output = new BinaryOut(outputFile);
		BinaryOut FrequencyFile = new BinaryOut(freqFile);
		
		//builts frequency table as an int array
		int[] freqTable = new int[256]; //array of size equal to # of characters (128 or 256, couldn't find which was right so I went with the bigger)
		int hold; //holds values of char
		while(!input.isEmpty()){
			hold = input.readChar(); //converts from char to ascii key value
			freqTable[hold] += 1; //holds frequency of a char at the index of the char's ascii	
		}
		PriorityQueue<node> pq = new PriorityQueue<>();
		//goes through table and adds elements to priority queue
		for(int i = 0; i < 256; i++) {
			//if the frequency at the index isn't 0, it adds a node to pq with key i and value of i's frequency
			if(freqTable[i] != 0) {
				node x = new node((char)i,freqTable[i],null,null);
				pq.add(x);
			}
		}
		//tests to see if queue is properly constructed
//		for(int i = 0; i < pq.size(); i++) {
//			System.out.println(pq.poll().key);
//		}
		//actual tree construction
		//to read from priority queue, take the top two numbers off the head and use them to construct a new node with null value and freq equal to the sum of the two, then put back into queue. queue keep lowest numbers at head 
		//since poll takes head, and the pq is from least to greatest, the top number should be the right most node (least frequency)
		node parent = new node('\0',0,null,null); //assigned outside so it can be accessed for createHashMap
		while(pq.size() > 1) { //gives me an error for >0 but not >1, probably because 1 is too short for poll to be called twice
			node rightchild = pq.poll();
			node leftchild = pq.poll();
			//parent made with frequency of sum of children. 0 used for value since null is not an int. Also assigns 
			parent = new node('\0', rightchild.key + leftchild.key, leftchild, rightchild);
			pq.add(parent);
		}
		
		
		//creates map to assign Strings to characters
		Map<Character, String> map = new HashMap<>();
		//create hashmap by going through tree until it reaches leaves, adding string along the way, and add them to the hashmap
		//created a method so it can be called recursively
		String str = "";
		createHashMap(parent,str,map);
		
		//MAP IS WRONG HERE
		//System.out.print(map.get('o'));
		
		//use map to encode file
		//goes through input file and for each char, writes a new char based on map, iterated through char to write booleans (to compress)
		while(!(inputDuplicate.isEmpty())) {
			String s = map.get(inputDuplicate.readChar());
			for(int i = 0; i<s.length();i++) {
				if (s.charAt(i)=='0') {
					output.write(false);
				}
				else {
					output.write(true);
				}
			}
			//output.write(map.get(inputDuplicate.readChar()));
		}
		output.flush();
		//create freq key table using map
//			FrequencyFile.flush();
//			FrequencyFile.write(";"+ "\n");
		for(int i = 0; i < 256; i++) {
			//if the frequency at the index isn't 0, it adds a node to pq with key i and value of i's frequency
			if(freqTable[i] != 0) {
				//converts ASCII integer value of a character to binary to store in file w/ freqTable element
				FrequencyFile.write(Integer.toBinaryString(i) + ":" + freqTable[i] + "\n");
				//semicolon makes reading it easier for the decoding :P
			}
			FrequencyFile.flush();
		}
	}
		
		
		
	
	public void createHashMap(node node, String string, Map<Character,String> map) {
		//if not a leaf, call recursively on both children to find leaves
		if(node.rightchild != null && node.leftchild != null) {
			createHashMap(node.leftchild, string + '0',map); //adds 0 when it goes down left branch
			createHashMap(node.rightchild, string +'1', map);//adds 1 when it goes down right branch
		}
		map.put(node.value, string); //(char) changes the number to its ascii char value
	}
	public void createDecodeHashMap(node node, String string, Map<String, Character> map2) {
		if(node.rightchild != null && node.leftchild != null) {
			createDecodeHashMap(node.leftchild, string + '0',map2); //adds 0 when it goes down left branch
			createDecodeHashMap(node.rightchild, string +'1', map2);//adds 1 when it goes down right branch
		}
		map2.put(string,node.value);
			
	}


   public void decode(String inputFile, String outputFile, String freqFile){
		BinaryIn input = new BinaryIn(inputFile);
		BinaryIn inputDuplicate = new BinaryIn(inputFile);
		BinaryOut output = new BinaryOut(outputFile);
		BinaryIn freqInput = new BinaryIn(freqFile);
		PriorityQueue<node> pq = new PriorityQueue<>();
		//map to create a "key" with
		Map<String, String> map = new HashMap<>();
		char x;
		//reads through Freq file and generate a map as a key
		String BinaryStr = "";
		String FreqStr = "";
		String temp = "";
		while(!freqInput.isEmpty()) {
			x = freqInput.readChar();
			if(x==':') {
				BinaryStr = temp; //assigns current string to binary
				temp = "";
			}
			else if(x!='\n') {
				temp += x; //adds char to string
			}
			else{
				FreqStr = temp; //assigns freqstr as the current string
				map.put(FreqStr, BinaryStr); //puts frequency as key with binary as value
				int FreqInt = Integer.parseInt(FreqStr); //hold frequencies as ints
				char c = (char)(Integer.parseInt(BinaryStr,2));//converts the string binary to an int and then to the char value
				node newNode = new node(c,FreqInt,null,null);
				pq.add(newNode);
				temp = "";
			}
		}
		//made to check nodes in queue
//		Iterator<node> it = pq.iterator();
//		while(it.hasNext()) {
//			node pp = it.next();
//		}
		//creates binary tree using priorityqueue
		node parent = new node('\0',0,null,null); //root node
		while(pq.size() > 1) { //gives me an error for >0 but not >1, probably because 1 is too short for poll to be called twice
			node rightchild = pq.poll();
			node leftchild = pq.poll();
			
		
			//parent made with frequency of sum of children. 0 used for value since null is not an int. Also assigns 
			parent = new node('\0', rightchild.key + leftchild.key, leftchild, rightchild);
			pq.add(parent);
		}
		
		//reads through input file and finds corresponding code form hashmap
		//goes down right if 1 and left if 0 until it reaches leaf node
		node hold = parent; //holds root node
		Boolean bool = null;
//		while(!input.isEmpty()) {
//			while(parent.leftchild != null && parent.rightchild != null) {
//				if(!input.isEmpty()) {
//					bool = input.readBoolean();
//				}
//				if(bool) {
//					parent=parent.rightchild;
//				}
//				else {
//					parent=parent.leftchild;
//				}
//			}
//			//catch necessary to prevent extra character from being written
//			if(input.isEmpty()) {
//				break;
//			}
//			output.write((char)parent.value);
//			parent = hold;
//		}
		int length = 0;
		while(!inputDuplicate.isEmpty()) {
			inputDuplicate.readBoolean();
			length++;
		}
		while(length>0){
			parent = hold;
			while(parent.leftchild != null && parent.rightchild != null) {
				if(!input.isEmpty()) {
					bool = input.readBoolean();
					length--;
				}
				if(bool) {
					parent=parent.rightchild;
				}
				else if (!bool) {
					parent=parent.leftchild;
				}
			}
			output.write(parent.value);
		}
		//I think flush might be adding extra characters to the jpg (because of the 8 bit rule) but I don't know how I would fix this
		output.flush();
		
   }





 

}
class Main{
	public static void main(String[] args){
	      Huffman  huffman = new HuffmanEncoderAndDecoder();
	      huffman.encode("roko.txt", "roko.enc", "freq.txt");
	      huffman.decode("roko.enc", "roko_dec.txt", "freq.txt");
			// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
			// On linux and mac, you can use `diff' command to check if they are the same. 
	   }
}
/**
 *  <i>Binary input</i>. This class provides methods for reading
 *  in bits from a binary input stream, either
 *  one bit at a time (as a {@code boolean}),
 *  8 bits at a time (as a {@code byte} or {@code char}),
 *  16 bits at a time (as a {@code short}),
 *  32 bits at a time (as an {@code int} or {@code float}), or
 *  64 bits at a time (as a {@code double} or {@code long}).
 *  <p>
 *  The binary input stream can be from standard input, a filename,
 *  a URL name, a Socket, or an InputStream.
 *  <p>
 *  All primitive types are assumed to be represented using their 
 *  standard Java representations, in big-endian (most significant
 *  byte first) order.
 *  <p>
 *  The client should not intermix calls to {@code BinaryIn} with calls
 *  to {@code In}; otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne

/**
 *  <i>Binary output</i>. This class provides methods for converting
 *  primtive type variables ({@code boolean}, {@code byte}, {@code char},
 *  {@code int}, {@code long}, {@code float}, and {@code double})
 *  to sequences of bits and writing them to an output stream.
 *  The output stream can be standard output, a file, an OutputStream or a Socket.
 *  Uses big-endian (most-significant byte first).
 *  <p>
 *  The client must {@code flush()} the output stream when finished writing bits.
 *  <p>
 *  The client should not intermix calls to {@code BinaryOut} with calls
 *  to {@code Out}; otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
final class BinaryOut {

    private BufferedOutputStream out;  // the output stream
    private int buffer;                // 8-bit buffer of bits to write out
    private int n;                     // number of bits remaining in buffer


   /**
     * Initializes a binary output stream from standard output.
     */
    public BinaryOut() {
        out = new BufferedOutputStream(System.out);
    }

   /**
     * Initializes a binary output stream from an {@code OutputStream}.
     * @param os the {@code OutputStream}
     */
    public BinaryOut(OutputStream os) {
        out = new BufferedOutputStream(os);
    }

   /**
     * Initializes a binary output stream from a file.
     * @param filename the name of the file
     */
    public BinaryOut(String filename) {
        try {
            OutputStream os = new FileOutputStream(filename);
            out = new BufferedOutputStream(os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Initializes a binary output stream from a socket.
     * @param socket the socket
     */
    public BinaryOut(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            out = new BufferedOutputStream(os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


   /**
     * Writes the specified bit to the binary output stream.
     * @param x the bit
     */
    private void writeBit(boolean x) {
        // add bit to buffer
        buffer <<= 1;
        if (x) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        n++;
        if (n == 8) clearBuffer();
    } 

   /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the byte
     */
    private void writeByte(int x) {
        assert x >= 0 && x < 256;

        // optimized if byte-aligned
        if (n == 0) {
            try {
                out.write(x);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // otherwise write one bit at a time
        for (int i = 0; i < 8; i++) {
            boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

    // write out any remaining bits in buffer to the binary output stream, padding with 0s
    private void clearBuffer() {
        if (n == 0) return;
        if (n > 0) buffer <<= (8 - n);
        try {
            out.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        n = 0;
        buffer = 0;
    }

   /**
     * Flushes the binary output stream, padding 0s if number of bits written so far
     * is not a multiple of 8.
     */
    public void flush() {
        clearBuffer();
        try {
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Flushes and closes the binary output stream.
     * Once it is closed, bits can no longer be written.
     */
    public void close() {
        flush();
        try {
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


   /**
     * Writes the specified bit to the binary output stream.
     * @param x the {@code boolean} to write
     */
    public void write(boolean x) {
        writeBit(x);
    } 

   /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the {@code byte} to write.
     */
    public void write(byte x) {
        writeByte(x & 0xff);
    }

   /**
     * Writes the 32-bit int to the binary output stream.
     * @param x the {@code int} to write
     */
    public void write(int x) {
        writeByte((x >>> 24) & 0xff);
        writeByte((x >>> 16) & 0xff);
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the <em>r</em>-bit int to the binary output stream.
     *
     * @param  x the {@code int} to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless {@code r} is between 1 and 32
     * @throws IllegalArgumentException unless {@code x} is between 0 and 2<sup>r</sup> - 1
     */
    public void write(int x, int r) {
        if (r == 32) {
            write(x);
            return;
        }
        if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value for r = " + r);
        if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }


   /**
     * Writes the 64-bit double to the binary output stream.
     * @param x the {@code double} to write
     */
    public void write(double x) {
        write(Double.doubleToRawLongBits(x));
    }

   /**
     * Writes the 64-bit long to the binary output stream.
     * @param x the {@code long} to write
     */
    public void write(long x) {
        writeByte((int) ((x >>> 56) & 0xff));
        writeByte((int) ((x >>> 48) & 0xff));
        writeByte((int) ((x >>> 40) & 0xff));
        writeByte((int) ((x >>> 32) & 0xff));
        writeByte((int) ((x >>> 24) & 0xff));
        writeByte((int) ((x >>> 16) & 0xff));
        writeByte((int) ((x >>>  8) & 0xff));
        writeByte((int) ((x >>>  0) & 0xff));
    }

   /**
     * Writes the 32-bit float to the binary output stream.
     * @param x the {@code float} to write
     */
    public void write(float x) {
        write(Float.floatToRawIntBits(x));
    }

   /**
     * Write the 16-bit int to the binary output stream.
     * @param x the {@code short} to write.
     */
    public void write(short x) {
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the 8-bit char to the binary output stream.
     *
     * @param  x the {@code char} to write
     * @throws IllegalArgumentException unless {@code x} is betwen 0 and 255
     */
    public void write(char x) {
        if (x < 0 || x >= 256) throw new IllegalArgumentException("Illegal 8-bit char = " + x);
        writeByte(x);
    }

   /**
     * Writes the <em>r</em>-bit char to the binary output stream.
     *
     * @param  x the {@code char} to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless {@code r} is between 1 and 16
     * @throws IllegalArgumentException unless {@code x} is between 0 and 2<sup>r</sup> - 1
     */
    public void write(char x, int r) {
        if (r == 8) {
            write(x);
            return;
        }
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value for r = " + r);
        if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

   /**
     * Writes the string of 8-bit characters to the binary output stream.
     *
     * @param  s the {@code String} to write
     * @throws IllegalArgumentException if any character in the string is not
     *         between 0 and 255
     */
    public void write(String s) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i));
    }


   /**
     * Writes the string of <em>r</em>-bit characters to the binary output stream.
     * @param  s the {@code String} to write
     * @param  r the number of relevants bits in each character
     * @throws IllegalArgumentException unless r is between 1 and 16
     * @throws IllegalArgumentException if any character in the string is not
     *         between 0 and 2<sup>r</sup> - 1
     */
    public void write(String s, int r) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i), r);
    }


   /**
     * Test client. Read bits from standard input and write to the file
     * specified on command line.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create binary output stream to write to file
        String filename = args[0];
        BinaryOut out = new BinaryOut(filename);
        BinaryIn  in  = new BinaryIn();

        // read from standard input and write to file
        while (!in.isEmpty()) {
            char c = in.readChar();
            out.write(c);
        }
        out.flush();
    }

}

 final class BinaryIn {
    private static final int EOF = -1;   // end of file

    private BufferedInputStream in;      // the input stream
    private int buffer;                  // one character buffer
    private int n;                       // number of bits left in buffer

   /**
     * Initializes a binary input stream from standard input.
     */
    public BinaryIn() {
        in = new BufferedInputStream(System.in);
        fillBuffer();
    }

   /**
     * Initializes a binary input stream from an {@code InputStream}.
     *
     * @param is the {@code InputStream} object
     */
    public BinaryIn(InputStream is) {
        in = new BufferedInputStream(is);
        fillBuffer();
    }

   /**
     * Initializes a binary input stream from a socket.
     *
     * @param socket the socket
     */
    public BinaryIn(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

   /**
     * Initializes a binary input stream from a URL.
     *
     * @param url the URL
     */
    public BinaryIn(URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + url);
        }
    }

   /**
     * Initializes a binary input stream from a filename or URL name.
     *
     * @param name the name of the file or URL
     */
    public BinaryIn(String name) {

        try {
            // first try to read file from local file system
            File file = new File(name);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                in = new BufferedInputStream(fis);
                fillBuffer();
                return;
            }

            // next try for files included in jar
            URL url = getClass().getResource(name);

            // or URL from web
            if (url == null) {
                url = new URL(name);
            }

            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + name);
        }
    }

    private void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;
        }
        catch (IOException e) {
            System.err.println("EOF");
            buffer = EOF;
            n = -1;
        }
    }

    /**
     * Returns true if this binary input stream exists.
     *
     * @return {@code true} if this binary input stream exists;
     *         {@code false} otherwise
     */
    public boolean exists()  {
        return in != null;
    }

   /**
     * Returns true if this binary input stream is empty.
     *
     * @return {@code true} if this binary input stream is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return buffer == EOF;
    }

   /**
     * Reads the next bit of data from this binary input stream and return as a boolean.
     *
     * @return the next bit of data from this binary input stream as a {@code boolean}
     * @throws NoSuchElementException if this binary input stream is empty
     */
    public boolean readBoolean() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
        if (n == 0) fillBuffer();
        return bit;
    }

   /**
     * Reads the next 8 bits from this binary input stream and return as an 8-bit char.
     *
     * @return the next 8 bits of data from this binary input stream as a {@code char}
     * @throws NoSuchElementException if there are fewer than 8 bits available
     */
    public char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        // special case when aligned byte
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        // combine last N bits of current buffer with first 8-N bits of new buffer
        int x = buffer;
        x <<= (8 - n);
        int oldN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n = oldN;
        x |= (buffer >>> n);
        return (char) (x & 0xff);
        // the above code doesn't quite work for the last character if N = 8
        // because buffer will be -1
    }


   /**
     * Reads the next <em>r</em> bits from this binary input stream and return
     * as an <em>r</em>-bit character.
     *
     * @param  r number of bits to read
     * @return the next {@code r} bits of data from this binary input streamt as a {@code char}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available
     * @throws IllegalArgumentException unless {@code 1 <= r <= 16}
     */
    public char readChar(int r) {
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 8 case
        if (r == 8) return readChar();

        char x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }


   /**
     * Reads the remaining bytes of data from this binary input stream and return as a string. 
     *
     * @return the remaining bytes of data from this binary input stream as a {@code String}
     * @throws NoSuchElementException if this binary input stream is empty or if the number of bits
     *         available is not a multiple of 8 (byte-aligned)
     */
    public String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        StringBuilder sb = new StringBuilder();
        while (!isEmpty()) {
            char c = readChar();
            sb.append(c);
        }
        return sb.toString();
    }


   /**
     * Reads the next 16 bits from this binary input stream and return as a 16-bit short.
     *
     * @return the next 16 bits of data from this binary input stream as a {@code short}
     * @throws NoSuchElementException if there are fewer than 16 bits available
     */
    public short readShort() {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 32 bits from this binary input stream and return as a 32-bit int.
     *
     * @return the next 32 bits of data from this binary input stream as a {@code int}
     * @throws NoSuchElementException if there are fewer than 32 bits available
     */
    public int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next <em>r</em> bits from this binary input stream return
     * as an <em>r</em>-bit int.
     *
     * @param  r number of bits to read
     * @return the next {@code r} bits of data from this binary input stream as a {@code int}
     * @throws NoSuchElementException if there are fewer than r bits available
     * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
     */
    public int readInt(int r) {
        if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 32 case
        if (r == 32) return readInt();

        int x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }

   /**
     * Reads the next 64 bits from this binary input stream and return as a 64-bit long.
     *
     * @return the next 64 bits of data from this binary input stream as a {@code long}
     * @throws NoSuchElementException if there are fewer than 64 bits available
     */
    public long readLong() {
        long x = 0;
        for (int i = 0; i < 8; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 64 bits from this binary input stream and return as a 64-bit double.
     *
     * @return the next 64 bits of data from this binary input stream as a {@code double}
     * @throws NoSuchElementException if there are fewer than 64 bits available
     */
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

   /**
     * Reads the next 32 bits from this binary input stream and return as a 32-bit float.
     *
     * @return the next 32 bits of data from this binary input stream as a {@code float}
     * @throws NoSuchElementException if there are fewer than 32 bits available
     */
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


   /**
     * Reads the next 8 bits from this binary input stream and return as an 8-bit byte.
     *
     * @return the next 8 bits of data from this binary input stream as a {@code byte}
     * @throws NoSuchElementException if there are fewer than 8 bits available
     */
    public byte readByte() {
        char c = readChar();
        return (byte) (c & 0xff);
    }
    
   /**
     * Unit tests the {@code BinaryIn} data type.
     * Reads the name of a file or URL (first command-line argument)
     * and writes it to a file (second command-line argument).
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BinaryIn  in  = new BinaryIn(args[0]);
        BinaryOut out = new BinaryOut(args[1]);

        // read one 8-bit char at a time
        while (!in.isEmpty()) {
            char c = in.readChar();
            out.write(c);
        }
        out.flush();
    }

}

 interface Huffman {

/**
 *     Encodes the input file using Huffman Coding. Produces two files 
 *     
 *     @param inputFile The name of the input file to be encoded.
 *          Do not modify this file.  
 *
 *     @param outputFile The name of the output file  (after encoding)
 *                This would be a binary file.
 *                If the file already exists, overwrite it.   
 *
 *     @param freqFile  Stores the frequency of each byte 
 *          This file is a text file 
 *          where each row contains texual representation 
 *          of each byte and the  number of occurence of this byte
 *          separated by ':' 
 *          An example entry would look like:
 *          01100001:12345
 *          Which means 
 *          the letter a (ascii code 097, binary representation 01100001)
 *          has occureed 12345. This file does not need to be sorted. 
 *          If this file already exists, overwrite.   
 *                     */
   public void encode(String inputFile, String outputFile, String freqFile);
   
/**
 *     Decodes the input file (which is the output of encoding()) 
 *     using Huffman decoding.  
 *     
 *     @param inputFile The name of the input file to be decoded. 
 *     Do not modify this file. 
 *
 *     @param outputFile The name of the output file  (after decoding)
 *
 *     @param freqFile  freqFile produced after encoding. 
 *     Do not modify this file. 
 *                     */
   public void decode(String inputFile, String outputFile, String freqFile);
}

