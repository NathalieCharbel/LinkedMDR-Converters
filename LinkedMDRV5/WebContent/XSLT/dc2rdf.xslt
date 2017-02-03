<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="document" select="generate-id()" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
	<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'"/>
	<xsl:template match="/">
		<rdf:RDF 
			xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
			xmlns:LinkedMDR="http://www.co-ode.org/ontologies/LinkedMDR.owl#"
			xmlns:DC="http://www.co-ode.org/ontologies/LinkedMDR.owl#DC:">
			<rdf:Description
				rdf:about="{concat('http://www.co-ode.org/ontologies/LinkedMDR.owl#',$document)}">
				<rdf:type rdf:resource="http://www.co-ode.org/ontologies/LinkedMDR.owl#Document"/>
				<xsl:for-each select="DCmetadata/*">
					<LinkedMDR:hasProperty rdf:resource="{concat('http://www.co-ode.org/ontologies/LinkedMDR.owl#', generate-id(.))}"/>
				</xsl:for-each>
			</rdf:Description>
			<xsl:for-each select="DCmetadata/*">
				<rdf:Description
					rdf:about="{concat('http://www.co-ode.org/ontologies/LinkedMDR.owl#', generate-id(.))}">
					<rdf:type rdf:resource="{concat('http://www.co-ode.org/ontologies/LinkedMDR.owl#','DC:',translate(substring(local-name(.), 1, 1), $lowercase, $uppercase),substring(local-name(.),2,string-length(local-name(.))-1))}"/>
					<xsl:choose>
						<xsl:when test="local-name(.)='date' or local-name(.)='created' or local-name(.)='modified'">
							<LinkedMDR:dateValue rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">
								<xsl:value-of select="."></xsl:value-of>
							</LinkedMDR:dateValue>
						</xsl:when>
						<xsl:otherwise>
							<LinkedMDR:stringValue rdf:datatype="http://www.w3.org/2001/XMLSchema#string">
								<xsl:value-of select="."></xsl:value-of>
							</LinkedMDR:stringValue>
						</xsl:otherwise>
					</xsl:choose>
				</rdf:Description>
			</xsl:for-each>
		</rdf:RDF>
	</xsl:template>
</xsl:stylesheet>