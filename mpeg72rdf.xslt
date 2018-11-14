<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:mpeg7="http://mpeg7.org/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lmdr="http://spider.sigappfr.org/linkedmdr/#" xmlns:tei="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#" >
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    
    <xsl:variable name="document">
        <xsl:apply-templates select="//mpeg7:MediaUri" />
    </xsl:variable>
    <xsl:variable name="image" select="concat($document,'.image')" />
    <xsl:variable name="mainstillregion" select="concat($document,'.stillregion.','main')" />
    
    <xsl:template match="/">
        <!--This is to resolve the namespace when & is used within attribute values-->	
        <xsl:text disable-output-escaping="yes"><![CDATA[<!DOCTYPE rdf:RDF [
		<!ENTITY lmdr "http://spider.sigappfr.org/linkedmdr/#" >
		<!ENTITY mpeg7 "http://mpeg7.org/" >
		<!ENTITY tei "http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#" >
		<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >]>]]>
		</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
		 xmlns:lmdr=&quot;http://spider.sigappfr.org/linkedmdr/#&quot;
		 xmlns:mpeg7=&quot;http://mpeg7.org/&quot;
		 xmlns:tei=&quot;http://www.tei-c.org/release/doc/tei-p5-doc/en/html/#&quot;&gt;
		</xsl:text>
        <xsl:text disable-output-escaping="yes">
            &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$document"/><xsl:text disable-output-escaping="yes">&quot;&gt;
				&lt;rdf:type rdf:resource=&quot;&amp;lmdr;Document&quot;/&gt;
				&lt;lmdr:hasPart_Object&gt;
					&lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$image"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					   &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;Image&quot;/&gt;
					     &lt;lmdr:hasPart_mpeg7Image&gt;
					       &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="$mainstillregion"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					          &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;StillRegion&quot;/&gt;</xsl:text>
                                 <xsl:apply-templates select="//mpeg7:Descriptor" />
                                 <xsl:apply-templates select="//mpeg7:VisualDescriptor[parent::mpeg7:Image]" />
                                 <xsl:apply-templates select="//mpeg7:StillRegion"/>
                                 <xsl:apply-templates select="//mpeg7:Semantic/mpeg7:SemanticBase[@xsi:type='ObjectType']" />
					        <xsl:text disable-output-escaping="yes">&lt;/rdf:Description&gt;
					     &lt;/lmdr:hasPart_mpeg7Image&gt;
					&lt;/rdf:Description&gt;
				&lt;/lmdr:hasPart_Object&gt;
			&lt;/rdf:Description&gt;</xsl:text>
        <xsl:text disable-output-escaping="yes">&lt;/rdf:RDF&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:Semantic/mpeg7:SemanticBase">
                                    <xsl:text disable-output-escaping="yes">
                                    &lt;lmdr:isAssociatedTo&gt;
                                         &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.semanticdescription.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					                       &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;SemanticDescription&quot;/&gt;
                                              &lt;lmdr:hasPart_mpeg7SemanticDescription&gt; 
                                                   &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.object.',generate-id(./mpeg7:Label/mpeg7:Name))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
                                                       &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;Object&quot;/&gt;
                                                       &lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="./mpeg7:Label/mpeg7:Name"/>
                                                       <xsl:text disable-output-escaping="yes">&lt;/lmdr:hasValue&gt;
                                                   &lt;/rdf:Description&gt;
                                              &lt;/lmdr:hasPart_mpeg7SemanticDescription&gt;  
                                         &lt;/rdf:Description&gt;
                                    &lt;/lmdr:isAssociatedTo&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:StillRegion">s
                                    <xsl:text disable-output-escaping="yes">
                                    &lt;lmdr:hasPart_mpeg7StillRegion&gt;
                                         &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.stillregion.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					                       &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;</xsl:text><xsl:value-of select="local-name(.)"/><xsl:text disable-output-escaping="yes">&quot;/&gt;</xsl:text>
                                            <xsl:apply-templates select="./mpeg7:VisualDescriptor" /> 
                                            <xsl:apply-templates select="./mpeg7:TextAnnotation/mpeg7:FreeTextAnnotation" />
                                          <xsl:text disable-output-escaping="yes">
                                         &lt;/rdf:Description&gt;
                                    &lt;/lmdr:hasPart_mpeg7StillRegion&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:TextAnnotation/mpeg7:FreeTextAnnotation">
                                    <xsl:text disable-output-escaping="yes">
                                    &lt;lmdr:hasPart_mpeg7StillRegion&gt;
                                         &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.imageannotation.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					                       &lt;rdf:type rdf:resource=&quot;&amp;lmdr;ImageAnnotation&quot;/&gt;
                                               &lt;lmdr:hasPart_Object&gt;
                                                   &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.sentence.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
                                                       &lt;rdf:type rdf:resource=&quot;&amp;tei;s&quot;/&gt;
                                                       &lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="."/><xsl:text disable-output-escaping="yes">&lt;/lmdr:hasValue&gt;
                                                   &lt;/rdf:Description&gt;  
                                               &lt;/lmdr:hasPart_Object&gt; 
                                         &lt;/rdf:Description&gt;
                                    &lt;/lmdr:hasPart_mpeg7StillRegion&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:StillRegion[count(*)=1]">
                                <xsl:text disable-output-escaping="yes">
                                    &lt;lmdr:hasPart_mpeg7StillRegion&gt;
                                         &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.textstillregion.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					                       &lt;rdf:type rdf:resource=&quot;&amp;lmdr;Text</xsl:text><xsl:value-of select="local-name(.)"/><xsl:text disable-output-escaping="yes">&quot;/&gt;
                                               &lt;lmdr:hasPart_Object&gt;
                                                   &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.sentence.',generate-id(./mpeg7:TextAnnotation/mpeg7:FreeTextAnnotation))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
                                                       &lt;rdf:type rdf:resource=&quot;&amp;tei;s&quot;/&gt;
                                                       &lt;lmdr:hasValue&gt;</xsl:text><xsl:value-of select="./mpeg7:TextAnnotation/mpeg7:FreeTextAnnotation"/><xsl:text disable-output-escaping="yes">&lt;/lmdr:hasValue&gt;
                                                   &lt;/rdf:Description&gt;  
                                               &lt;/lmdr:hasPart_Object&gt; 
                                         &lt;/rdf:Description&gt;
                                    &lt;/lmdr:hasPart_mpeg7StillRegion&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor|mpeg7:VisualDescriptor">
                                    <xsl:text disable-output-escaping="yes">
                                    &lt;lmdr:hasPart_mpeg7StillRegion&gt;
                                         &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.visualdescriptor.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
					                       &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;</xsl:text><xsl:value-of select="substring-before(@xsi:type,'Type')"/><xsl:text disable-output-escaping="yes">&quot;/&gt;</xsl:text>
					                           <xsl:apply-templates select="./*"></xsl:apply-templates>   
                                                <xsl:text disable-output-escaping="yes">
                                         &lt;/rdf:Description&gt;
                                    &lt;/lmdr:hasPart_mpeg7StillRegion&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor/*|mpeg7:VisualDescriptor/*">
        <xsl:call-template name="elementToDataProperty">
            <xsl:with-param name="descriptorProperty" select="."></xsl:with-param>
        </xsl:call-template>                                             
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor[@xsi:type='DominantColorType']/mpeg7:Value|mpeg7:VisualDescriptor[@xsi:type='DominantColorType']/mpeg7:Value">
                                               <xsl:text disable-output-escaping="yes">
                                               &lt;lmdr:hasPart_mpeg7DominantColor&gt;
                                                    &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.colorvalue.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
                                                        &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;ColorValue&quot;/&gt;</xsl:text>
                                                        <xsl:apply-templates select="./*" />
                                                    <xsl:text disable-output-escaping="yes">
                                                    &lt;/rdf:Description&gt;
                                               &lt;/lmdr:hasPart_mpeg7DominantColor&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor[@xsi:type='DominantColorType']/mpeg7:Value/*|mpeg7:VisualDescriptor[@xsi:type='DominantColorType']/mpeg7:Value/*">
       <xsl:call-template name="elementToDataProperty">
           <xsl:with-param name="descriptorProperty" select="."></xsl:with-param>
       </xsl:call-template>                              
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor[@xsi:type='DominantColorType']/mpeg7:ColorQuantization|mpeg7:VisualDescriptor[@xsi:type='DominantColorType']/mpeg7:ColorQuantization">
                                               <xsl:apply-templates select="./mpeg7:Component"></xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="mpeg7:Descriptor[@xsi:type='DominantColorType']/mpeg7:ColorQuantization/mpeg7:Component|mpeg7:VisualDescriptor[@xsi:type='DominantColorType']/mpeg7:ColorQuantization/mpeg7:Component">
                                               <xsl:text disable-output-escaping="yes">
                                               &lt;lmdr:hasPart_mpeg7DominantColor&gt;
                                                    &lt;rdf:Description rdf:about=&quot;&amp;lmdr;</xsl:text><xsl:value-of select="concat($document,'.colorquantizationcomponent.',generate-id(.))"/><xsl:text disable-output-escaping="yes">&quot;&gt;
                                                        &lt;rdf:type rdf:resource=&quot;&amp;mpeg7;ColorQuantizationComponent&quot;/&gt;
                                                           &lt;mpeg7:ColorQuantizationComponentType&gt;</xsl:text>
                                                                <xsl:value-of select="." />
                                                            <xsl:text disable-output-escaping="yes">
                                                           &lt;/mpeg7:ColorQuantizationComponentType&gt;
                                                           &lt;lmdr:hasValue&gt;</xsl:text>
                                                                <xsl:value-of select="./following::mpeg7:NumOfBins[1]" />
                                                            <xsl:text disable-output-escaping="yes">
                                                           &lt;/lmdr:hasValue&gt;
                                                    &lt;/rdf:Description&gt;
                                               &lt;/lmdr:hasPart_mpeg7DominantColor&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template name="elementToDataProperty">
        <xsl:param name="descriptorProperty" />
                                             <xsl:text disable-output-escaping="yes">
                                             &lt;mpeg7:</xsl:text><xsl:value-of select="local-name($descriptorProperty)"/><xsl:text disable-output-escaping="yes">&gt;</xsl:text><xsl:value-of select="$descriptorProperty"/>
                                             <xsl:text disable-output-escaping="yes">
                                             &lt;/mpeg7:</xsl:text><xsl:value-of select="local-name($descriptorProperty)"/><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
    </xsl:template>
    
    <!--Get the name of a document from the MediaURI inside mpeg7 element-->
    <xsl:template match="mpeg7:MediaUri[parent::mpeg7:Mpeg7]">
        <xsl:call-template name="substring-after-last">
            <xsl:with-param name="string" select="./text()" />
            <xsl:with-param name="delimiter" select="'\'" />
        </xsl:call-template>
    </xsl:template>
    <!--Do not do anything-->
    <xsl:template match="//mpeg7:MediaUri[parent::mpeg7:MediaLocator]" />
    <!--Get the name of a document from the MediaURI inside MediaLocator element child of MediaProfile-->
    <xsl:template match="//mpeg7:MediaProfile[@master='true']//mpeg7:MediaUri">
        <xsl:call-template name="substring-after-last">
            <xsl:with-param name="string" select="./text()" />
            <xsl:with-param name="delimiter" select="'/'" />
        </xsl:call-template> 
    </xsl:template>
    
    <!--Substring after last occurrence of a string-->
    <xsl:template name="substring-after-last">
        <xsl:param name="string" />
        <xsl:param name="delimiter" />
        <xsl:choose>
            <xsl:when test="contains($string, $delimiter)">
                <xsl:call-template name="substring-after-last">
                    <xsl:with-param name="string"
                        select="substring-after($string, $delimiter)" />
                    <xsl:with-param name="delimiter" select="$delimiter" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise><xsl:value-of 
                select="$string" /></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>