package edu.asu.conceptpower.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.util.URICreator;

public class XMLTypeMessage extends AXMLMessage {

	public void appendEntry(ConceptType type, ConceptType supertype) {
		StringBuffer sb = new StringBuffer();

		// start entry
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.TYPE_ENTRY + ">");

		// type uri, id
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE
				+ " ");
		if (type != null) {
			sb.append(XMLConstants.TYPE_ID_ATTR + "=\"" + type.getTypeId()
					+ "\" ");
			sb.append(XMLConstants.TYPE_URI_ATTR + "=\""
					+ URICreator.getTypeURI(type) + "\"");
		}
		sb.append(">");
		if (type != null)
			sb.append(StringEscapeUtils.escapeXml(type.getTypeName()));
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.TYPE + ">");

		// type description
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.DESCRIPTION + ">");
		sb.append(StringEscapeUtils.escapeXml(type.getDescription()));
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.DESCRIPTION + ">");

		// creator id
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.CREATOR_ID + ">");
		sb.append(StringEscapeUtils.escapeXml(type.getCreatorId() != null ? type
				.getCreatorId().trim() : ""));
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.CREATOR_ID + ">");

		// matches
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.MATCHES + ">");
		sb.append(StringEscapeUtils.escapeXml(type.getMatches() != null ? type
				.getMatches().trim() : ""));
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.MATCHES + ">");

		// modified by
		sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.MODIFIED_BY + ">");
		sb.append(StringEscapeUtils.escapeXml(type.getModified() != null ? type
				.getModified().trim() : ""));
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.MODIFIED_BY + ">");

		// supertype
		if (supertype != null) {
			sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":"
					+ XMLConstants.SUPERTYPE + " ");

			sb.append(XMLConstants.TYPE_ID_ATTR + "=\"" + supertype.getTypeId()
					+ "\" ");
			sb.append(XMLConstants.TYPE_URI_ATTR + "=\""
					+ URICreator.getTypeURI(supertype) + "\"");

			sb.append(">");
			sb.append(StringEscapeUtils.escapeXml(supertype.getTypeName()));
			sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
					+ XMLConstants.SUPERTYPE + ">");
		}

		// end entry
		sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":"
				+ XMLConstants.TYPE_ENTRY + ">");

		this.addEntry(sb.toString());
	}
}
