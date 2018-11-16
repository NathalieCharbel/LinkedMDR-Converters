<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tei="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:lmdr="http://spider.sigappfr.org/linkedmdr/#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="document">
		<xsl:apply-templates select="tei:TEI/tei:teiHeader/tei:fileDesc/tei:sourceDesc/tei:p[contains(text(),'URI:')]"></xsl:apply-templates>
	</xsl:variable>
	<xsl:variable name="text" select="concat($document,'.text.',generate-id(tei:TEI/tei:text))" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
	<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" /> 
	
	<xsl:template match="/">
	<!--This is to resolve the namespace when & is used within attribute values-->	
	<xsl:text disable-output-escaping="yes"><![CDATA[<!DOCTYPE rdf:RDF [
		<!ENTITY lmdr "http://spider.sigappfr.org/linkedmdr/#" >
		<!ENTITY tei "http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#" >
		<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >]>]]>
	</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
		 xmlns:lmdr=&quot;http://spider.sigappfr.org/linkedmdr/#&quot;
		 xmlns:tei=&quot;http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#&quot;&gt;
		</xsl:text>
			<xsl:text disable-output-escaping="yes">
			&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$document"/><xsl:text disable-output-escaping="yes">&quot;&gt;
				&lt;rdf:type rdf:resource=&quot;&amp;lmdr;Document&quot;/&gt;
				&lt;lmdr:hasPart_Object&gt;
					&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$text"/><xsl:text disable-output-escaping="yes">&quot;&gt;
						&lt;rdf:type rdf:resource=&quot;&amp;tei;text&quot;/&gt;
					&lt;/rdf:Description&gt;
				&lt;/lmdr:hasPart_Object&gt;
			&lt;/rdf:Description&gt;</xsl:text>
			<xsl:apply-templates select="tei:TEI/tei:text/tei:body" />
		<xsl:text disable-output-escaping="yes">&lt;/rdf:RDF&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template match="tei:body">
		<!--case any child node-->
		<xsl:apply-templates select="node()" />
	</xsl:template>
	
	<!--case only element nodes-->
	<xsl:template match="child::*">
	 	<xsl:if test="not(local-name(.)='body')">   
		<xsl:choose>
			<xsl:when test="local-name(..)='body'">
				<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$text"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;lmdr:hasPart_tei</xsl:text>
			<xsl:value-of select="Text"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;lmdr:hasPart_tei</xsl:text>
			<xsl:call-template name="capitalizeFirstLetter">
				<xsl:with-param name="conceptName" select="local-name(..)" />
			</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>	
			<xsl:text disable-output-escaping="yes">&gt;
				&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;tei;</xsl:text>
					<xsl:value-of select="local-name(.)"/>
					<xsl:text disable-output-escaping="yes">&quot;/&gt;
				&lt;/rdf:Description&gt;</xsl:text>
		<xsl:choose>
			<xsl:when test="local-name(..)='body'">
			<xsl:text disable-output-escaping="yes">&lt;/lmdr:hasPart_tei</xsl:text>
				<xsl:value-of select="Text"/>
			</xsl:when>
			<xsl:otherwise><xsl:text disable-output-escaping="yes">&lt;/lmdr:hasPart_tei</xsl:text>
			<xsl:call-template name="capitalizeFirstLetter">
				<xsl:with-param name="conceptName" select="local-name(..)" />
			</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>		

			<xsl:text disable-output-escaping="yes">&gt;
		&lt;/rdf:Description&gt;</xsl:text>
		<xsl:apply-templates select="node()" />
		</xsl:if>
	</xsl:template>
	
	<!--case only text nodes-->
	<xsl:template match="child::text()">
		<xsl:choose>
			<xsl:when test="local-name(..)!='body'">
				<xsl:text disable-output-escaping="yes">
				&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="." /><xsl:text disable-output-escaping="yes">
					&lt;/lmdr:hasValue&gt;
				&lt;/rdf:Description&gt;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!--case TEI:hi-->
	<xsl:template match="tei:hi">
		<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="child::text()" /><xsl:text disable-output-escaping="yes">
			&lt;/lmdr:hasValue&gt;
		&lt;/rdf:Description&gt;</xsl:text>
	</xsl:template>
	
	<!--Capitalize the first letter of a tei element-->
	<xsl:template name="capitalizeFirstLetter">
		<xsl:param name="conceptName" />
		<xsl:value-of select="concat(translate(substring($conceptName, 1, 1), $lowercase, $uppercase),substring($conceptName,2,string-length($conceptName)-1))"/>
	</xsl:template>
	
	<!--Get the name of a document from the source description element of TEI header-->
	<xsl:template match="tei:TEI/tei:teiHeader/tei:fileDesc/tei:sourceDesc/tei:p">
		<xsl:value-of select="substring-after(./text(),'uploads\')"/>
	</xsl:template>
	
</xsl:stylesheet>