package seedu.addressbook.storage;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.storage.Storage.InvalidStorageFilePathException;
import seedu.addressbook.storage.Storage.StorageOperationException;
import seedu.addressbook.storage.jaxb.AdaptedAddressBook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class StorageStub extends Storage{
	
	 public static class InvalidStorageFilePathException extends IllegalValueException {
	        public InvalidStorageFilePathException(String message) {
	            super(message);
	        }
	    }

	    /**
	     * Signals that some error has occured while trying to convert and read/write data between the application
	     * and the storage file.
	     */
	    public static class StorageOperationException extends Exception {
	        public StorageOperationException(String message) {
	            super(message);
	        }
	    }
	    
	    public StorageStub() throws InvalidStorageFilePathException {
	        this(DEFAULT_STORAGE_FILEPATH);
	    }
	    
	    public StorageStub(String filePath) throws InvalidStorageFilePathException {
	        try {
	            jaxbContext = JAXBContext.newInstance(AdaptedAddressBook.class);
	        } catch (JAXBException jaxbe) {
	            throw new RuntimeException("jaxb initialisation error");
	        }

	        path = Paths.get(filePath);
	        if (!isValidPath(path)) {
	            throw new InvalidStorageFilePathException("Storage file should end with '.txt'");
	        }
	    }
	    
	    public void save(AddressBook addressBook) {

	    }
	    
	    public AddressBook load() throws StorageOperationException {
	        try (final Reader fileReader =
	                     new BufferedReader(new FileReader(path.toFile()))) {

	            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	            final AdaptedAddressBook loaded = (AdaptedAddressBook) unmarshaller.unmarshal(fileReader);
	            // manual check for missing elements
	            if (loaded.isAnyRequiredFieldMissing()) {
	                throw new StorageOperationException("File data missing some elements");
	            }
	            return loaded.toModelType();

	        /* Note: Here, we are using an exception to create the file if it is missing. However, we should minimize
	         * using exceptions to facilitate normal paths of execution. If we consider the missing file as a 'normal'
	         * situation (i.e. not truly exceptional) we should not use an exception to handle it.
	         */

	        // create empty file if not found
	        } catch (FileNotFoundException fnfe) {
	            final AddressBook empty = new AddressBook();
	            save(empty);
	            return empty;

	        // other errors
	        } catch (IOException ioe) {
	            throw new StorageOperationException("Error writing to file: " + path);
	        } catch (JAXBException jaxbe) {
	            throw new StorageOperationException("Error parsing file data format");
	        } catch (IllegalValueException ive) {
	            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
	        }
	    }

	    public String getPath() {
	        return path.toString();
	    }
	    
     
}
