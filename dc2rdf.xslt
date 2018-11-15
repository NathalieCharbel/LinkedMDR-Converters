<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/terms/" xmlns:lmdr="http://spider.sigappfr.org/linkedmdr/#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="document" select="substring-after(DCmetadata/dc:identifier/text(),'uploads\')" />
	<xsl:template match="/">
	 <xsl:text disable-output-escaping="yes"><![CDATA[<!DOCTYPE rdf:RDF [
		<!ENTITY lmdr "http://spider.sigappfr.org/linkedmdr/#" >
		<!ENTITY dc "http://purl.org/dc/terms/" >
		<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >]>]]>
	</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
		 xmlns:lmdr=&quot;http://spider.sigappfr.org/linkedmdr/#&quot;
		 xmlns:dc=&quot;http://purl.org/dc/terms/#&quot;
		 xmlns:rdfs=&quot;http://www.w3.org/2000/01/rdf-schema#&quot;&gt;
			&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$document"/>
			<xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;lmdr;Document&quot;/&gt;</xsl:text>
				<xsl:for-each select="DCmetadata/*">
					<xsl:text disable-output-escaping="yes">
					&lt;lmdr:hasProperty rdf:resource=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;/&gt;</xsl:text>
				</xsl:for-each>
			<xsl:text disable-output-escaping="yes">
			&lt;/rdf:Description&gt;</xsl:text>
			<xsl:for-each select="DCmetadata/*">
				<xsl:text disable-output-escaping="yes">
				&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/>	
					<xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;dc;</xsl:text><xsl:value-of select="local-name(.)"/><xsl:text disable-output-escaping="yes">&quot;/&gt;
					&lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="."></xsl:value-of>
					<xsl:text disable-output-escaping="yes">&lt;lmdr:hasValue&gt;
				&lt;/rdf:Description&gt;</xsl:text>
			</xsl:for-each>
		<xsl:text disable-output-escaping="yes">&lt;/rdf:RDF&gt;</xsl:text>
	</xsl:template>
	
</xsl:stylesheet>