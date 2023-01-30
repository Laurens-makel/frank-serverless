<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <members>
            <xsl:for-each select="/result/rowset/row">
                <member>
                    <xsl:attribute name="name"><xsl:value-of select="field[@name='NAME']" /></xsl:attribute>
                </member>
            </xsl:for-each>
        </members>
    </xsl:template>

</xsl:stylesheet>