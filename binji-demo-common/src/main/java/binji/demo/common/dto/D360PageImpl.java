package binji.demo.common.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/**
 * @author jesse keane
 *
 * @param <T>
 */
@Getter
@Setter
public class D360PageImpl<T> implements Page<T> {

	private static final long serialVersionUID = -9140436817486047602L;

	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Pageable pageable;
	private List<T> content;
	private int totalPages;
	private long totalElements;
	private int size;
	private int number;
	private Sort sort;
	private int numberOfElements;
	private boolean first;
	private boolean last;



	public D360PageImpl(List<T> content, Pageable pageable, long total) {
		this.content = content;
		this.pageable = pageable;
		this.totalElements = !content.isEmpty() && pageable != null && pageable.getOffset() + pageable.getPageSize() > total
				? pageable.getOffset() + content.size() : total;
		this.size = content.size();
		this.numberOfElements = content.size();
		this.number = pageable.getOffset();
		this.first = number == 0 ? true : false;
		this.last = number == getTotalPages() ? true : false;
		
	}

	public D360PageImpl(List<T> content) {
		this.content = content;
		this.totalElements = content.size();
	}


	public D360PageImpl() {
		this.content = new ArrayList<T>();
	}


	public boolean hasNext() {
		return getNumber() + 1 < getTotalPages();
	}


	@Override
	public int getTotalPages() {		
		return totalPages == 0 ? getSize() == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) getSize()) : totalPages;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String contentType = "UNKNOWN";
		List<T> content = getContent();

		if (content.size() > 0) {
			contentType = content.get(0).getClass().getName();
		}

		return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
	}
	
	
	
	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

	@Override
	public boolean hasPrevious() {
		return getNumber() > 0;
	}

	@Override
	public Pageable nextPageable() {
		return hasNext() ? pageable.next() : null;
	}

	@Override
	public Pageable previousPageable() {
		if (hasPrevious()) {
			return pageable.previousOrFirst();
		}

		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public long getTotalElements() {
		return totalElements;
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		return new D360PageImpl<S>(getConvertedContent(converter), pageable, totalElements);
	}
	
	protected <S> List<S> getConvertedContent(Converter<? super T, ? extends S> converter) {

		Assert.notNull(converter, "Converter must not be null!");

		List<S> result = new ArrayList<S>(content.size());

		for (T element : this) {
			result.add(converter.convert(element));
		}

		return result;
	}

}
