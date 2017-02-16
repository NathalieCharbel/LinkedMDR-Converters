<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:LinkedMDR="http://www.co-ode.org/ontologies/LinkedMDR.owl#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="document" select="substring-after(DCmetadata/dc:identifier/text(),'uploads\')" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
	<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'"/>
	<xsl:template match="/">
	 <xsl:text disable-output-escaping="yes"><![CDATA[<!DOCTYPE rdf:RDF [
		<!ENTITY LinkedMDR "http://www.co-ode.org/ontologies/LinkedMDR.owl#" >
		<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >]>]]>
	</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
		 xmlns:LinkedMDR=&quot;http://www.co-ode.org/ontologies/LinkedMDR.owl#&quot;
		 xmlns:DC=&quot;&amp;LinkedMDR;DC:&quot;
		 xmlns:rdfs=&quot;http://www.w3.org/2000/01/rdf-schema#&quot;&gt;
			&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="$document"/>
			<xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;LinkedMDR;Document&quot;/&gt;</xsl:text>
				<xsl:for-each select="DCmetadata/*">
					<xsl:text disable-output-escaping="yes">
					&lt;LinkedMDR:hasProperty rdf:resource=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;/&gt;</xsl:text>
				</xsl:for-each>
			<xsl:text disable-output-escaping="yes">
			&lt;/rdf:Description&gt;</xsl:text>
			<xsl:for-each select="DCmetadata/*">
				<xsl:text disable-output-escaping="yes">
				&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/>	
					<xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;LinkedMDR;DC:</xsl:text><xsl:value-of select="concat(translate(substring(local-name(.), 1, 1), $lowercase, $uppercase),substring(local-name(.),2,string-length(local-name(.))-1))"/><xsl:text disable-output-escaping="yes">&quot;/&gt;
					&lt;LinkedMDR:hasValue&gt;</xsl:text><xsl:value-of select="."></xsl:value-of>
					<xsl:text disable-output-escaping="yes">&lt;/LinkedMDR:hasValue&gt;
				&lt;/rdf:Description&gt;</xsl:text>
			</xsl:for-each>
		<xsl:text disable-output-escaping="yes">&lt;/rdf:RDF&gt;</xsl:text>
	</xsl:template>
	
</xsl:stylesheet>