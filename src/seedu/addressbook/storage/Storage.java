package seedu.addressbook.storage;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;


public abstract class Storage {
	 public static String DEFAULT_STORAGE_FILEPATH = "addressbook.txt";

	    public JAXBContext jaxbContext;

	    public Path path;
	    
	    public static class InvalidStorageFilePathException extends IllegalValueException {
	        public InvalidStorageFilePathException(String message) {
	            super(message);
	        }
	    }
	    
	    public static class StorageOperationException extends Exception {
	        public StorageOperationException(String message) {
	            super(message);
	        }
	    }
	    
	    public static boolean isValidPath(Path filePath) {
	        return filePath.toString().endsWith(".txt");
	    }
	    
	    public abstract void save(AddressBook addressBook) throws StorageOperationException;
	    
	    public abstract AddressBook load() throws StorageOperationException, seedu.addressbook.storage.StorageStub.StorageOperationException;
	    
	    public abstract String getPath();
	    
}
