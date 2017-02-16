<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:TEI="http://www.tei-c.org/ns/1.0" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:LinkedMDR="http://www.co-ode.org/ontologies/LinkedMDR.owl#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="document">
		<xsl:apply-templates select="TEI:TEI/TEI:teiHeader/TEI:fileDesc/TEI:sourceDesc/TEI:p[contains(text(),'URI:')]"></xsl:apply-templates>
	</xsl:variable>
	<xsl:variable name="text" select="concat($document,'.text.',generate-id(TEI:TEI/TEI:text))" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
	<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" /> 
	
	<xsl:template match="/">
	<!--This is to resolve the namespace when & is used within attribute values-->	
	<xsl:text disable-output-escaping="yes"><![CDATA[<!DOCTYPE rdf:RDF [
		<!ENTITY LinkedMDR "http://www.co-ode.org/ontologies/LinkedMDR.owl#" >
		<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >]>]]>
	</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
		 xmlns:LinkedMDR=&quot;http://www.co-ode.org/ontologies/LinkedMDR.owl#&quot;
		 xmlns:hasPart_TEI=&quot;&amp;LinkedMDR;hasPart_TEI:&quot;&gt;
		</xsl:text>
			<xsl:text disable-output-escaping="yes">
			&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="$document"/><xsl:text disable-output-escaping="yes">&quot;&gt;
				&lt;rdf:type rdf:resource=&quot;&amp;LinkedMDR;Document&quot;/&gt;
				&lt;LinkedMDR:hasPart_Object&gt;
					&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="$text"/><xsl:text disable-output-escaping="yes">&quot;&gt;
						&lt;rdf:type rdf:resource=&quot;&amp;LinkedMDR;TEI:Text&quot;/&gt;
					&lt;/rdf:Description&gt;
				&lt;/LinkedMDR:hasPart_Object&gt;
			&lt;/rdf:Description&gt;</xsl:text>
			<xsl:apply-templates select="TEI:TEI/TEI:text/TEI:body" />
		<xsl:text disable-output-escaping="yes">&lt;/rdf:RDF&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template match="TEI:Body">
		<!--case any child node-->
		<xsl:apply-templates select="node()" />
	</xsl:template>
	
	<!--case only element nodes-->
	<xsl:template match="child::*">
		<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;hasPart_TEI:</xsl:text>
			<xsl:choose>
				<xsl:when test="local-name(..)='p'">
					<xsl:call-template name="pToParagraph"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="capitalizeFirstLetter">
						<xsl:with-param name="conceptName" select="local-name(..)" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text disable-output-escaping="yes">&gt;
				&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(.),'.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					&lt;rdf:type rdf:resource=&quot;&amp;LinkedMDR;TEI:</xsl:text>
					<xsl:choose>
						<xsl:when test="local-name(.)='p'">
							<xsl:call-template name="pToParagraph"></xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="capitalizeFirstLetter">
								<xsl:with-param name="conceptName" select="local-name(.)" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text disable-output-escaping="yes">&quot;/&gt;
				&lt;/rdf:Description&gt;
			&lt;/hasPart_TEI:</xsl:text>
			<xsl:choose>
				<xsl:when test="local-name(..)='p'">
					<xsl:call-template name="pToParagraph"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="capitalizeFirstLetter">
						<xsl:with-param name="conceptName" select="local-name(..)" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text disable-output-escaping="yes">&gt;
		&lt;/rdf:Description&gt;</xsl:text>
		<xsl:apply-templates select="node()" />
	</xsl:template>
	
	<!--case only text nodes-->
	<xsl:template match="child::text()">
		<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;LinkedMDR:hasValue&gt;</xsl:text><xsl:value-of select="." /><xsl:text disable-output-escaping="yes">
			&lt;/LinkedMDR:hasValue&gt;
		&lt;/rdf:Description&gt;</xsl:text>
	</xsl:template>
	
	<!--case TEI:hi-->
	<xsl:template match="TEI:hi">
		<xsl:text disable-output-escaping="yes">
		&lt;rdf:Description rdf:about=&quot;&amp;LinkedMDR;</xsl:text><xsl:value-of select="concat($document,'.',local-name(..),'.',generate-id(..))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
			&lt;LinkedMDR:hasValue&gt;</xsl:text><xsl:value-of select="child::text()" /><xsl:text disable-output-escaping="yes">
			&lt;/LinkedMDR:hasValue&gt;
		&lt;/rdf:Description&gt;</xsl:text>
	</xsl:template>
	
	<!--case transform p into Paragraph-->
	<xsl:template name="pToParagraph">
		<xsl:value-of select="'Paragraph'"/>
	</xsl:template>
	
	<!--Capitalize the first letter of a tei element-->
	<xsl:template name="capitalizeFirstLetter">
		<xsl:param name="conceptName" />
		<xsl:value-of select="concat(translate(substring($conceptName, 1, 1), $lowercase, $uppercase),substring($conceptName,2,string-length($conceptName)-1))"/>
	</xsl:template>
	
	<!--Get the name of a document from the source description element of TEI header-->
	<xsl:template match="TEI:TEI/TEI:teiHeader/TEI:fileDesc/TEI:sourceDesc/TEI:p">
		<xsl:value-of select="substring-after(./text(),'uploads\')"/>
	</xsl:template>
	
</xsl:stylesheet>