/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.extendcomment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;
import jakarta.inject.Inject;

/**
 * Tests the markers of the comment notification service.
 */
public class CommentNotificationServiceTest extends LuteceTestCase
{
    @Inject
    private ICommentNotificationService _service;

    /**
     * Every marker offered to the configuration has a value taken from the comment.
     */
    @Test
    public void testMarkersValues( )
    {
        Timestamp date = new Timestamp( 1_700_000_000_000L );
        Comment comment = new Comment( );
        comment.setDateComment( date );
        comment.setComment( "Nice" );
        comment.setIdExtendableResource( "42" );
        comment.setName( "Alice" );
        comment.setEmail( "alice@example.org" );

        Map<String, Object> values = _service.getAvailableMarkersValues( comment );
        ReferenceList markers = _service.getAvailableMarkers( Locale.FRENCH );

        assertEquals( markers.size( ), values.size( ) );
        markers.forEach( marker -> assertEquals( true, values.containsKey( marker.getCode( ) ), marker.getCode( ) ) );
        assertEquals( date, values.get( "date_creation" ) );
        assertEquals( "Nice", values.get( "comment" ) );
        assertEquals( "42", values.get( "resourceId" ) );
        assertEquals( "Alice", values.get( "firstNameUser" ) );
        assertEquals( "alice@example.org", values.get( "emailUser" ) );
    }
}
