<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/elements/1.1/">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
            <DCmetadata xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:dcterms="http://purl.org/dc/terms/">
             
                <xsl:for-each select="/xhtml:html/xhtml:head/xhtml:meta">
                    <xsl:choose> 
                        <xsl:when test="@name='dc:title' or
                            @name='dc:creator' or @name='dc:subject' or 
                            @name='dc:description'
                            or @name='dc:date' or @name='dc:type' or @name='dc:format'
                            or @name='dcterms:references' or @name='dcterms:requires' or
                            @name='dcterms:temporal' or @name='dcterms:spatial'
                            or @name='dc:relation' or @name='dc:publisher'
                            or @name='dcterms:created' or @name='dcterms:modified'
                            or @name='dc:rights' or @name='dc:coverage' or @name='dc:language'"> 
                            <xsl:element name="{@name}">
                                <xsl:value-of select="@content"/>
                            </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>           
                        </xsl:otherwise> 
                    </xsl:choose>
                </xsl:for-each>
                
            </DCmetadata> 
    </xsl:template>
</xsl:stylesheet>