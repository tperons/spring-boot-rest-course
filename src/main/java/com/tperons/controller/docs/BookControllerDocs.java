package com.tperons.controller.docs;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.tperons.dto.BookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Books", description = "Endpoints to Manage Books")
@ApiResponses({
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
public interface BookControllerDocs {

    @Operation(summary = "Finds all Books", description = "Returns a list with all books.", tags = {
            "Books" }, responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))) }), })
    ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
            @Parameter(description = "Page number") Integer page,
            @Parameter(description = "Page size") Integer size,
            @Parameter(description = "Ordering direction") String direction,
            @Parameter(hidden = true) PagedResourcesAssembler<BookDTO> assembler);

    @Operation(summary = "Finds a Book by ID", description = "Returns a specific book by their ID.", tags = {
            "Books" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = BookDTO.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content) })
    ResponseEntity<BookDTO> findById(@Parameter(description = "ID of book to be found") Long id);

    @Operation(summary = "Finds Books by Title", description = "Returns a list of books by their title.", tags = {
            "Books" }, responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))) }), })
    ResponseEntity<PagedModel<EntityModel<BookDTO>>> findByTitle(
            @Parameter(description = "Title") String title,
            @Parameter(description = "Page number") Integer page,
            @Parameter(description = "Page size") Integer size,
            @Parameter(description = "Ordering direction") String direction,
            @Parameter(hidden = true) PagedResourcesAssembler<BookDTO> assembler);

    @Operation(summary = "Adds a New Book", description = "Adds a new book by passing in a JSON representation of the book.", tags = {
            "Books" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = BookDTO.class))), })
    ResponseEntity<BookDTO> create(@RequestBody(description = "Book data to create") BookDTO obj);

    @Operation(summary = "Updates a Book's Information", description = "Updates a book's information by passing in a JSON representation of the updated book.", tags = {
            "Books" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = BookDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), })
    ResponseEntity<BookDTO> update(@Parameter(description = "ID of the book to be updated") Long id,
            @RequestBody(description = "Updated book data") BookDTO obj);

    @Operation(summary = "Deletes a Book", description = "Deletes a specific book by their ID", tags = {
            "People" }, responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), })
    ResponseEntity<Void> delete(@Parameter(description = "ID of the book to be deleted") Long id);

}
