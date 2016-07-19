/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched when the client's IP address matches a given pattern.
 */
public class ClientIpCondition extends AbstractClientCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3636436056399797050L;
	
	/** Magic number. */
    private static final int CONST_32 = 32;
	/** Magic number. */
	private static final int CONST_8 = 8;

	/**
     * The pattern the client should match.
     */
    private String pattern;
    
    /**
     * Empty constructor (for Digester).
     */
    public ClientIpCondition() {
        super();
    }
    
    /**
     * Set the pattern.
     * @param pattern a pattern, such as:
     * - 148.60.
     * - 148.60.0.0/255.255.0.0
     * - 148.60.0.0/16
     */
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractClientCondition
	 * #isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			final InetAddress client) {
    	boolean result = false;
    	String maskConfig = null;
    	String ipClient = null;
    	String ip = null;
    	String mask = null;
    	
    	if (this.pattern.endsWith(".")) {
    		if (client.getHostAddress().startsWith(this.pattern)) {
    			result = true;
    		}
    	}
    	
    	String[] splitPattern = this.pattern.split("/");
    	if (splitPattern.length > 1) {
    		ip = splitPattern[0];
    		mask = splitPattern[1];
    	} else {
    		ip = splitPattern[0];
    	}
    	
    	String ipConfig = ip2binary(ip);
    	
    	if (mask != null) {
    		maskConfig = maskToBinaryString(mask);
    	}
    	
   		ipClient = ip2binary(client.getHostAddress());
    	
    	if (mask != null && ipConfig != null) {
    		result = ipCheck(ipClient, ipConfig, maskConfig);
    	}
    	return result;
    }
    	
    /**
     * @throws DepartmentSelectionCompileError 
     * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
     */
    @Override
	public void checkInternal() throws DepartmentSelectionCompileError {
        if (this.pattern == null) {
            throw new DepartmentSelectionCompileError("<ip> tags should have a 'pattern' attribute");
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return "<ip pattern=\"" + pattern + "\" />";
    }
    
    /**
     * (left) pad a string with 0s up to 8 characters.
     * @param binaryString
     * @return a string.
     */
    private String formatBinaryString(final String binaryString) {
    	String result = binaryString;
        while (result.length() < CONST_8) {
            result = "0".concat(result);
        }
        return result;
    }
    
    /**
     * format an ip to a binary ip (ex: 255.255.255.0 to 11111111111111111111111100000000).
     * @param ip
     * @return String ip in a binary format
     */
    private String ip2binary(final String ip) {
        String[] splited = ip.split("\\.");
        StringBuffer res = new StringBuffer(formatBinaryString(Integer.toBinaryString(Integer.parseInt(splited[0]))));
        for (int i = 1; i < splited.length; i++) {
            res.append(formatBinaryString(Integer.toBinaryString(Integer.parseInt(splited[i]))));
        }
        return res.toString();
    }
    
    /**
     * convert a 'normal' mask to a binary format (ex: 255.255.255.0 to 11111111111111111111111100000000 or 24 
     * to 11111111111111111111111100000000).
     * @param mask the mask 
     * @return String 
     */

    private String maskToBinaryString(final String mask) {
        StringBuffer binaryMask = new StringBuffer();          
        String[] splitedMask = mask.split("\\.");
        if (splitedMask.length <= 1) {
            for (int i = 0; i < Integer.parseInt(mask); i++) {
                binaryMask.append("1");
            }
            //maskBinary = formatBinaryString(Integer.toBinaryString(Integer.parseInt(maskSplited[0])));
            while (binaryMask.length() < CONST_32) {
                binaryMask.append("0");
            }
        } else {
	        binaryMask.append(formatBinaryString(Integer.toBinaryString(Integer.parseInt(splitedMask[0]))));
	        for (int i = 1; i < splitedMask.length; i++) {
	            binaryMask.append(formatBinaryString(Integer.toBinaryString(Integer.parseInt(splitedMask[i]))));
	        }
        }
        return binaryMask.toString();
    }

    /**
     * return true if the logic and between ipClient/mask and ipConfig/mask have the same result.
     * @param ipClient
     * @param ipConfig
     * @param mask
     * @return boolean true if logic and of ipClient/mask and ipConfig/mask have the same result
     */
    private boolean ipCheck(final String ipClient, final String ipConfig, final String mask) {
        String clientNet = binaryAnd(ipClient, mask);
        String configNet = binaryAnd(ipConfig, mask);
        
        return clientNet.equals(configNet);
    }
    
    /**
     * own implementation of a binary 'AND'.
     * @param ip
     * @param mask
     * @return String the logic and of an ip and a mask
     */
    private String binaryAnd(final String ip, final String mask) {
        
        char[] ipChar = ip.toCharArray(); 
        char[] maskChar = mask.toCharArray();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < maskChar.length && i < ipChar.length; i++) {
            if (maskChar[i] == '1' && ipChar[i] == '1') {
                result.append("1");
            } else {
                result.append("0");
            }
        }
        return result.toString();
    }

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "ip";
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

}

