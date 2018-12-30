package com.ralphavalon.autodoc.validator;

import com.ralphavalon.autodoc.args.Args;

public interface Validator {
	
	boolean isValidArgs(Args parsedArgs);
	
}
