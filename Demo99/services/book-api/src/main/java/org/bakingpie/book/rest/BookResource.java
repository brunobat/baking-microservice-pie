/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bakingpie.book.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bakingpie.book.domain.Book;
import org.bakingpie.book.repository.BookRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@ApplicationScoped
@Path("books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "books", description = "Operations for Books.")
public class BookResource {

    // ======================================
    // =             Injection              =
    // ======================================

    @Inject
    private BookRepository bookRepository;

    // ======================================
    // =              Methods               =
    // ======================================

    @GET
    @Path("{id}")
    @ApiOperation(
        value = "Find a Book by the Id.",
        response = Book.class)
    @ApiResponses(@ApiResponse(code = 404, message = "Not Found"))
    public Response findById(@PathParam("id") final Long id) {
        return ofNullable(bookRepository.findById(id))
            .map(Response::ok)
            .orElse(status(NOT_FOUND))
            .build();
    }

    @GET
    @ApiOperation(
        value = "Find all Books",
        response = Book.class, responseContainer = "List")
    public Response findAll() {
        return ok(bookRepository.findAll()).build();
    }

    @POST
    @ApiOperation(
        value = "Create a Book",
        response = Book.class, code = SC_CREATED)
    public Response create(final Book book) {
        final Book created = bookRepository.create(book);
        return created(URI.create("books/" + created.getId())).build();
    }

    @PUT
    @Path("{id}")
    @ApiOperation(
        value = "Update a Book",
        response = Book.class)
    public Response update(@PathParam("id") final Long id, final Book book) {
        return ok(bookRepository.update(book)).build();
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(
        value = "Delete a Book",
        response = Book.class, code = SC_NO_CONTENT)
    public Response delete(@PathParam("id") final Long id) {
        bookRepository.deleteById(id);
        return noContent().build();
    }
}
