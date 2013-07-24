Nikos, Theodore,

Here-attached a new version of the simpleParser library that supports the new json internal metadata format ("AKIF").
This new version requires commons-io-2.4.jar and json-simple-1.1.1.jar (also included).

It can be used as follows:

		SimpleMetadata sm ;
		try 
		{
			sm = SimpleMetadataFactory.getSimpleMetadata( SimpleMetadataFactory.AKIF ) ;
			sm.load( path_to_metadata_file.json ) ;
			System.out.println( sm.toString() ) ;
		}
		catch (ParserException e) 
		{
				e.printStackTrace() ;
		}

Best regards,

David